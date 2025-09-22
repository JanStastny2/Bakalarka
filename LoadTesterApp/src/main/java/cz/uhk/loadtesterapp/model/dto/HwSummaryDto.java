package cz.uhk.loadtesterapp.model.dto;

public record HwSummaryDto(Double avgCpu, Double p95Cpu,
                           Double maxCpu, Double avgHeapMb,
                           Double maxHeapMb, Double avgJvmMemoryUsage,
                           Double maxJvmMemoryUsage) {

}
