package cz.uhk.grainweight.service;

import org.springframework.stereotype.Service;

@Service
public class WorkSimulator {

    public void simulateIoDelay(long delayMs) {
        if (delayMs <= 0) return;
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public void simulateCpuWork(long iterations) {
        if (iterations <= 0) return;
        long x = 0;
        for (long i = 0; i < iterations; i++) {
            x ^= (i * 31) + (x << 5);
        }
        if (x == 42) System.out.print("");
    }
}

