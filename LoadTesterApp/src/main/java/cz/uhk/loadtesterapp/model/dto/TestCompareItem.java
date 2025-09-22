package cz.uhk.loadtesterapp.model.dto;

import cz.uhk.loadtesterapp.model.enums.ProcessingMode;
import cz.uhk.loadtesterapp.model.enums.TestStatus;

import java.time.Instant;

public record TestCompareItem(
        Long id,
        TestStatus status,
        ProcessingMode processingMode,
        Integer concurrency,
        Integer poolSizeOrCap,
        Long delayMs,
        String effectiveUrl,
        Integer successes,
        Integer failures,
        Double successRate,
        Long durationMs,
        Double throughputRps,
        Double avgResponseTimeMs,
        Double p95ResponseTimeMs,
        Double avgServerProcessingMs,
        Double avgQueueWaitMs,
        Instant createdAt
) {

}