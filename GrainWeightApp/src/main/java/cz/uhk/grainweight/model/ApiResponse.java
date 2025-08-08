package cz.uhk.grainweight.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;
    private long durationMs;
    private T data;
    private String message;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponse(int status, long durationMs, T data, String message) {
        this.status = status;
        this.durationMs = durationMs;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}


