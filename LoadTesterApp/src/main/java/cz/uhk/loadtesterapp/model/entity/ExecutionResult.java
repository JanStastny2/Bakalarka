package cz.uhk.loadtesterapp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExecutionResult {
    private int status;
    private long clientLatencyMs;
    private Long serverProcessingMs;
    private Long queueWaitMs;
}
