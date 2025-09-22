package cz.uhk.loadtesterapp.model.dto;

import java.time.Instant;

public record HwSampleDto(Instant ts, Double cpu, Double heapMb) {
}
