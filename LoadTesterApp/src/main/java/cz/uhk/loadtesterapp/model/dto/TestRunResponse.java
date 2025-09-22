package cz.uhk.loadtesterapp.model.dto;

import cz.uhk.loadtesterapp.model.entity.HwSummary;
import cz.uhk.loadtesterapp.model.enums.ProcessingMode;
import cz.uhk.loadtesterapp.model.enums.TestStatus;

public record TestRunResponse(Long id, TestStatus status,
                              Integer totalRequests, Integer concurrency, ProcessingMode processingMode,
                              Integer poolSizeOrCap, Long delayMs,
                              String effectiveUrl, String errorMessage,
                              RequestDefinitionDto request,
                              TestSummaryDto summary,
                              HwSummary hwSummary,
                              java.time.Instant createdAt, java.time.Instant startedAt, java.time.Instant finishedAt,
                              TestRunResponse.SimpleUserRef createdBy) {
    public record SimpleUserRef(Long id, String username) {}
}

