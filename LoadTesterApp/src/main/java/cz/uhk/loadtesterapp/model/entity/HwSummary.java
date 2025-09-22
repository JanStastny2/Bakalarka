package cz.uhk.loadtesterapp.model.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HwSummary {

    private Double avgCpu;
    private Double p95Cpu;
    private Double maxCpu;

    private Double avgHeapMb;
    private Double maxHeapMb;

    private Double avgJvmMemoryUsage;
    private Double maxJvmMemoryUsage;
}
