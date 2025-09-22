package cz.uhk.loadtesterapp.service;

import cz.uhk.loadtesterapp.model.entity.HwSummary;
import cz.uhk.loadtesterapp.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class HwSampleServiceImpl implements HwSampleService {

    private final WebClient.Builder webClientBuilder;
    private final TestRepository testRepo;

    private static final Logger log = LoggerFactory.getLogger(HwSampleServiceImpl.class);

    private final Map<Long, Session> sessions = new ConcurrentHashMap<>();

    @Override
    public Disposable start(Long testId, URI actuatorBase, Duration interval) {
        var client = webClientBuilder.baseUrl(actuatorBase.toString()).build();
        var buf = Collections.synchronizedList(new ArrayList<Sample>());

        Disposable d = Flux.interval(interval)
                .flatMap(t -> readAll(client))
                .doOnNext(buf::add)
                .doOnError(ex -> log.warn("HW sampler failed (testId={}): {}", testId, ex.toString()))
                .onErrorContinue((ex, o) -> {})
                .subscribe();

        sessions.put(testId, new Session(d, buf));
        return d;
    }

    @Override
    public void stopAndSummarize(Long testId) {
        var session = sessions.remove(testId);
        if (session == null) return;

        try {
            session.d.dispose();
        } catch (Throwable ignored) {}

        var samples = new ArrayList<>(session.buf);
        if (samples.isEmpty()) return;

        // CPU
        var cpuVals  = samples.stream().map(s -> s.cpu).filter(Objects::nonNull).sorted().toList();
        Double avgCpu = avg(cpuVals);
        Double maxCpu = max(cpuVals);
        Double p95Cpu = p95(cpuVals);

        // Heap
        var heapVals = samples.stream().map(s -> s.heapMb).filter(Objects::nonNull).toList();
        Double avgHeap = avg(heapVals);
        Double maxHeap = max(heapVals);


        var jvmMemVals  = samples.stream().map(s -> s.jvmMemMb).filter(Objects::nonNull).toList();
        Double avgJvmMem = avg(jvmMemVals);
        Double maxJvmMem = max(jvmMemVals);

        var tr = testRepo.findById(testId).orElse(null);
        if (tr != null) {
            tr.setHwSummary(HwSummary.builder()
                    .avgCpu(avgCpu).p95Cpu(p95Cpu).maxCpu(maxCpu)
                    .avgHeapMb(avgHeap).maxHeapMb(maxHeap)
                    .avgJvmMemoryUsage(avgJvmMem).maxJvmMemoryUsage(maxJvmMem)
                    .build());
            testRepo.save(tr);
        }
    }

    private static Double bytesToMb(Double bytes) {
        return (bytes == null) ? null : (bytes / 1024d / 1024d);
    }

    private static Double sumNonNull(Double... vals) {
        double s = 0; int c = 0;
        for (Double v : vals) if (v != null) { s += v; c++; }
        return c == 0 ? null : s;
    }

    private Mono<Sample> readAll(WebClient client) {
        return Mono.zip(
                readMetric(client, "/actuator/metrics/system.cpu.usage").defaultIfEmpty(Double.NaN),
                readMetric(client, "/actuator/metrics/jvm.memory.used?tag=area:heap").defaultIfEmpty(Double.NaN),
                readMetric(client, "/actuator/metrics/jvm.memory.used?tag=area:nonheap").defaultIfEmpty(Double.NaN),
                readMetric(client, "/actuator/metrics/jvm.buffer.memory.used").defaultIfEmpty(Double.NaN)

        ).map(t -> {
            Double cpu  = toNullable(t.getT1());
            Double cpuPct = (cpu == null) ? null : Math.max(0, Math.min(100, cpu * 100.0));
            Double heapUsedMb = bytesToMb(toNullable(t.getT2()));
            Double nonHeapUsedMb = bytesToMb(toNullable(t.getT3()));
            Double buffersUsedMb = bytesToMb(toNullable(t.getT4()));

            Double jvmTotalUsedMb = sumNonNull(heapUsedMb, nonHeapUsedMb, buffersUsedMb);
            return new Sample(Instant.now(), cpuPct, heapUsedMb, jvmTotalUsedMb);

        }).onErrorResume(ex -> {
            log.debug("HW sampler zip error: {}", ex.toString());
            return Mono.just(new Sample(Instant.now(), null, null, null));
        });
    }

    private static Double toNullable(Double v) {
        return (v == null || v.isNaN()) ? null : v;
    }

    private Mono<Double> readMetric(WebClient client, String uri) {
        return client.get().uri(uri)
                .retrieve()
                .bodyToMono(MetricsResponse.class)
                .map(this::firstValue)
                .onErrorResume(ex -> {
                    log.debug("readMetric({}) failed: {}", uri, ex.toString());
                    return Mono.just(Double.NaN);
                });
    }

    private Double firstValue(MetricsResponse m) {
        if (m == null || m.measurements == null) return Double.NaN;
        return m.measurements.stream()
                .filter(mm -> "VALUE".equalsIgnoreCase(mm.statistic()))
                .map(Measurement::value)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(Double.NaN);
    }

    private static Double avg(List<Double> vals) {
        return vals.isEmpty() ? null : vals.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    }

    private static Double max(List<Double> vals) {
        return vals.isEmpty() ? null : vals.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);
    }

    private static Double p95(List<Double> valsSorted) {
        if (valsSorted.size() < 20) return null;
        int idx = (int) Math.floor(0.95 * (valsSorted.size() - 1));
        return valsSorted.get(idx);
    }

    private record Sample(Instant ts, Double cpu, Double heapMb, Double jvmMemMb) {}
    private record Measurement(String statistic, Double value) {}
    private record MetricsResponse(String name, List<Measurement> measurements) {}
    private record Session(Disposable d, List<Sample> buf) {}
}
