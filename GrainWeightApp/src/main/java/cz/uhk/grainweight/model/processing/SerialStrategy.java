package cz.uhk.grainweight.model.processing;

import org.springframework.stereotype.Component;

import java.util.concurrent.Semaphore;
import java.util.function.Supplier;

@Component
public class SerialStrategy implements ProcessingStrategy {

    private final Semaphore sem = new Semaphore(1, true);

    @Override
    public <T> ProcessingResult<T> execute(Supplier<T> task) {
        long qStart = System.nanoTime();
        try {
            sem.acquire();
            long start = System.nanoTime();
            long queueMs = (start - qStart) / 1_000_000;

            try {
                T data = task.get();
                long end = System.nanoTime();
                long procMs = (end - start) / 1_000_000;
                return new ProcessingResult<>(data, procMs, queueMs);
            } finally {
                sem.release();
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ie);
        }
    }
}

