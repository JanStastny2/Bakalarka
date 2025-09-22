package cz.uhk.loadtesterapp.model.dto;


import cz.uhk.loadtesterapp.model.entity.RequestDefinition;
import cz.uhk.loadtesterapp.model.enums.ProcessingMode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record TestUpdateRequest(
        @Min(1) @NotNull Integer totalRequests,
        @NotNull @Min(1) Integer concurrency,
        @NotNull ProcessingMode processingMode,
        @Min(1) Integer poolSizeOrCap,
        @NotNull RequestDefinition request,
        @Min(0) Long delayMs
)
{ }
