package cz.uhk.grainweight.rest;

import cz.uhk.grainweight.model.ApiResponse;
import cz.uhk.grainweight.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public abstract class BaseController {
    protected <T> ResponseEntity<ApiResponse<T>> wrapResponse(Supplier<T> action, HttpStatus successStatus, String successMessage) {
        long start = System.nanoTime();
        try {
            T result = action.get();
            long duration = (System.nanoTime() - start) / 1_000_000;

            ApiResponse<T> response = new ApiResponse<>(
                    successStatus.value(),
                    duration,
                    result,
                    successMessage
            );

            return new ResponseEntity<>(response, successStatus);
        }
        catch (Exception e) {
            long duration = (System.nanoTime() - start) / 1_000_000;
            ApiResponse<T> errorResponse = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    duration,
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }


}
