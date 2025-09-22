package cz.uhk.grainweight.rest;

import cz.uhk.grainweight.model.ApiResponse;
import cz.uhk.grainweight.model.processing.ProcessingResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public abstract class BaseController {
    protected <T> ResponseEntity<ApiResponse<T>> wrapResponse(
            Supplier<T> action,
            HttpStatus successStatus,
            String successMessage
    ) {
        long t0 = System.nanoTime();
        try {
            T data = action.get();
            long durationMs = (System.nanoTime() - t0) / 1_000_000;

            ApiResponse<T> body = ApiResponse.<T>builder()
                    .status(successStatus.value())
                    .durationMs(durationMs)
                    .data(data)
                    .message(successMessage)
                    .build();

            return ResponseEntity.status(successStatus).body(body);
        } catch (RuntimeException ex) {
            long durationMs = (System.nanoTime() - t0) / 1_000_000;

            ApiResponse<T> body = ApiResponse.<T>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .durationMs(durationMs)
                    .message(ex.getMessage())
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

    protected <T> ResponseEntity<ApiResponse<T>> wrapResponseProcessed(
            Supplier<ProcessingResult<T>> action,
            HttpStatus successStatus,
            String successMessage) {

        long t0 = System.nanoTime();
        try {
            ProcessingResult<T> pr = action.get();
            long durationMs = (System.nanoTime() - t0) / 1_000_000;

            ApiResponse<T> body = ApiResponse.<T>builder()
                    .status(successStatus.value())
                    .durationMs(durationMs)
                    .data(pr.getData())
                    .message(successMessage)
                    .serverProcessingMs(pr.getServerProcessingMs())
                    .queueWaitMs(pr.getQueueWaitMs())
                    .build();

            return ResponseEntity.status(successStatus)
                    .header("X-Server-Processing-Ms", String.valueOf(pr.getServerProcessingMs()))
                    .header("X-Queue-Wait-Ms", String.valueOf(pr.getQueueWaitMs()))
                    .body(body);
        } catch (RuntimeException ex) {
            long durationMs = (System.nanoTime() - t0) / 1_000_000;
            ApiResponse<T> body = ApiResponse.<T>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .durationMs(durationMs)
                    .message(ex.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }
}
