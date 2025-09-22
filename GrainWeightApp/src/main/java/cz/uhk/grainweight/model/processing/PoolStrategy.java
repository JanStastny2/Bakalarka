package cz.uhk.grainweight.model.processing;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Slf4j
@Component
public class PoolStrategy implements ProcessingStrategy {

    private static ExecutorService newFixedPool(int size, String name) {
        int n = Math.max(2, size);
        ThreadFactory tf = new ThreadFactory() {
            private final AtomicInteger idx = new AtomicInteger(1);
            @Override public Thread newThread(Runnable r) {
                Thread t = new Thread(r, name + "-" + idx.getAndIncrement());
                t.setDaemon(true);
                t.setUncaughtExceptionHandler((th, ex) ->
                        log.error("Uncaught in {}: {}", th.getName(), ex.toString(), ex));
                return t;
            }
        };
        return new ThreadPoolExecutor(
                n, n,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                tf
        );
    }

    private final ExecutorService pool =
            newFixedPool(Math.max(2, Runtime.getRuntime().availableProcessors() * 2), "gw-pool");

    private volatile Semaphore cap = null;
    private volatile int currentCap = 0;

    private final AtomicInteger running = new AtomicInteger(0);


    public synchronized void setCap(Integer size) {
        if (size == null || size < 1) {
            if (cap != null) {
                cap = null;
                currentCap = 0;
                log.info("POOL cap disabled");
            }
            return;
        }
        if (cap != null && currentCap == size) {
            return;
        }
        cap = new Semaphore(size, true);
        currentCap = size;
        log.info("POOL cap set to {}", size);
    }

    @Override
    public <T> ProcessingResult<T> execute(Supplier<T> task) {
        final long qStart = System.nanoTime();

        try {
            final Semaphore limiter = this.cap;
            if (limiter != null) {
                limiter.acquire();
            }

            return pool.submit(() -> {
                long start = System.nanoTime();
                long queueMs = (start - qStart) / 1_000_000;

                int now = running.incrementAndGet();
                log.info("RUNNING={} (cap={}) thread={}", now, currentCap, Thread.currentThread().getName());
                log.debug("POOL execute(cap={}, thread={})", currentCap, Thread.currentThread().getName());

                try {
                    T data = task.get();
                    long procMs = (System.nanoTime() - start) / 1_000_000;
                    return new ProcessingResult<>(data, procMs, queueMs);
                } finally {
                    int left = running.decrementAndGet();
                    log.info("DONE   -> RUNNING={} thread={}", left, Thread.currentThread().getName());
                    if (limiter != null) limiter.release();
                }
            }).get();

        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting for permit/result", ie);
        } catch (ExecutionException ee) {
            Throwable cause = ee.getCause() != null ? ee.getCause() : ee;
            if (cause instanceof RuntimeException re) throw re;
            throw new RuntimeException(cause);
        }
    }

    @PreDestroy
    public void shutdown() {
        pool.shutdown();
    }
}
