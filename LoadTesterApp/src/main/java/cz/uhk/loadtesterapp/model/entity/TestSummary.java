package cz.uhk.loadtesterapp.model.entity;


import jakarta.persistence.Embeddable;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class TestSummary {
    private int successes;
    private int failures;
    private double successRate;
    private long durationMs; //trvani testu
    private double throughputRps;

    private double avgResponseTimeMs; // latence klient
    private double p95ResponseTimeMs;

    private Double avgServerProcessingMs; // latence server
    private Double p95ServerProcessingMs;
    private Double avgQueueWaitMs;
    private Double p95QueueWaitMs;
}
