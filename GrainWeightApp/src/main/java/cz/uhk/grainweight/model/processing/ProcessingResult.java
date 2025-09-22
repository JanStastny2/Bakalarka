package cz.uhk.grainweight.model.processing;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProcessingResult<T> {
    private T data;
    private long serverProcessingMs;
    private long queueWaitMs;

}
