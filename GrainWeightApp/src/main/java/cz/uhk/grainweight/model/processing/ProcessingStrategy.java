package cz.uhk.grainweight.model.processing;

import java.util.function.Supplier;

public interface ProcessingStrategy {
    <T> ProcessingResult<T> execute(Supplier<T> task);
}
