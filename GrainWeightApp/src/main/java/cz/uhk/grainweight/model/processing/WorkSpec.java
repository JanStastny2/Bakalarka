package cz.uhk.grainweight.model.processing;

import lombok.Data;

@Data
public class WorkSpec {
    private ProcessingMode mode;
    private Integer size;
}
