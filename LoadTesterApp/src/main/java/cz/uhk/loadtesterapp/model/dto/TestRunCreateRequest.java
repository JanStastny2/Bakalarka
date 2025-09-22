package cz.uhk.loadtesterapp.model.dto;

import cz.uhk.loadtesterapp.model.enums.ProcessingMode;

public record TestRunCreateRequest(@jakarta.validation.constraints.Min(1) @jakarta.validation.constraints.NotNull Integer totalRequests,
                                   @jakarta.validation.constraints.Min(1) @jakarta.validation.constraints.NotNull Integer concurrency,
                                   @jakarta.validation.constraints.NotNull ProcessingMode processingMode,
                                   Integer poolSizeOrCap,
                                   Long delayMs,
                                   @jakarta.validation.constraints.NotNull RequestDefinitionDto request) {
}

