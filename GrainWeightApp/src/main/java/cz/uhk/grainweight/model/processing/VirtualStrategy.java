package cz.uhk.grainweight.model.processing;


import cz.uhk.grainweight.rest.WorkController;
import cz.uhk.grainweight.service.ProcessingRouter;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Slf4j
@Component
public class VirtualStrategy implements ProcessingStrategy {

    private final ExecutorService vexec = Executors.newVirtualThreadPerTaskExecutor();

    private volatile Semaphore cap = null;
    private volatile int currentCap = 0;

    private final AtomicInteger running = new AtomicInteger(0);

    public synchronized void setConcurrencyCap(Integer size) {
        if (size == null || size < 1) {
            if (cap != null) {
                cap = null;
                currentCap = 0;
                log.info("VIRT cap disabled");
            }
            return;
        }
        if (cap != null && currentCap == size) {
            return;
        }
        cap = new Semaphore(size, true);
        currentCap = size;
        log.info("VIRT cap set to {}", size);
    }

    @Override
    public <T> ProcessingResult<T> execute(Supplier<T> task) {
        final long qStart = System.nanoTime();
        try {
            final Semaphore limiter = this.cap;
            if (limiter != null) {
                limiter.acquire();
            }
            return vexec.submit(() -> {
                long start = System.nanoTime();
                long queueMs = (start - qStart) / 1_000_000;

                int now = running.incrementAndGet();
                log.info("VIRT RUNNING={} (cap={})", now, currentCap);

                try {
                    T data = task.get();
                    long procMs = (System.nanoTime() - start) / 1_000_000;
                    return new ProcessingResult<>(data, procMs, queueMs);
                } finally {
                    int left = running.decrementAndGet();
                    log.info("VIRT DONE -> RUNNING={}", left);
                    if (limiter != null) limiter.release();
                }
            }).get();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ie);
        } catch (ExecutionException ee) {
            Throwable c = ee.getCause() != null ? ee.getCause() : ee;
            if (c instanceof RuntimeException re) throw re;
            throw new RuntimeException(c);
        }
    }

    @PreDestroy
    public void shutdown() { vexec.shutdown(); }
}

