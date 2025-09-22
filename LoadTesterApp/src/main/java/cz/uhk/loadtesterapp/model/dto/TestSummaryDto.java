package cz.uhk.loadtesterapp.model.dto;

public record TestSummaryDto(Integer successes, Integer failures,
                             Double successRate, Long durationMs,
                             Double throughputRps, Double avgResponseTimeMs,
                             Double p95ResponseTimeMs, Double avgServerProcessingMs,
                             Double p95ServerProcessingMs, Double avgQueueWaitMs,
                             Double p95QueueWaitMs
                             ) {
}

