package cz.uhk.grainweight.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private long durationMs;
    private T data;
    private String message;

    private Long serverProcessingMs;
    private Long queueWaitMs;
}


