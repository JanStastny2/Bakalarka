package cz.uhk.loadtesterapp.model.entity;

import cz.uhk.loadtesterapp.model.enums.ProcessingMode;
import cz.uhk.loadtesterapp.model.enums.TestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "test_run")

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"createdBy", "request", "summary", "errorMessage"})
public class TestRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @Min(1)
    private Integer totalRequests;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private TestStatus status;

    @Column(nullable = false)
    private Integer concurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_mode", nullable = false, length = 32)
    @NotNull
    private ProcessingMode processingMode;

    @Column(name = "pool_size_or_cap")
    private Integer poolSizeOrCap;

    @Column(name = "effective_url", length = 2048)
    private String effectiveUrl;

    @Column(name = "error_message", length = 2048)
    private String errorMessage;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "req_url", nullable = false, length = 1024)),
            @AttributeOverride(name = "method", column = @Column(name = "req_method", nullable = false, length = 16)),
            @AttributeOverride(name = "contentType", column = @Column(name = "req_content_type", length = 128))
    })
    private RequestDefinition request;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "successes", column = @Column(name = "sum_successes")),
            @AttributeOverride(name = "failures", column = @Column(name = "sum_failures")),
            @AttributeOverride(name = "successRate", column = @Column(name = "sum_success_rate")),
            @AttributeOverride(name = "durationMs", column = @Column(name = "sum_duration_ms")),
            @AttributeOverride(name = "throughputRps", column = @Column(name = "sum_throughput_rps")),
            @AttributeOverride(name = "avgResponseTimeMs", column = @Column(name = "sum_avg_resp_ms")),
            @AttributeOverride(name = "p95ResponseTimeMs", column = @Column(name = "sum_p95_resp_ms")),
            @AttributeOverride(name = "avgServerProcessingMs", column = @Column(name = "sum_avg_srv_ms")),
            @AttributeOverride(name = "p95ServerProcessingMs", column = @Column(name = "sum_p95_srv_ms")),
            @AttributeOverride(name = "avgQueueWaitMs", column = @Column(name = "sum_avg_queue_ms")),
            @AttributeOverride(name = "p95QueueWaitMs", column = @Column(name = "sum_p95_queue_ms"))
    })
    private TestSummary summary;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "avgCpu", column = @Column(name = "hw_avg_cpu")),
            @AttributeOverride(name = "p95Cpu", column = @Column(name = "hw_p95_cpu")),
            @AttributeOverride(name = "maxCpu", column = @Column(name = "hw_max_cpu")),
            @AttributeOverride(name = "avgHeapMb", column = @Column(name = "hw_avg_heap_mb")),
            @AttributeOverride(name = "maxHeapMb", column = @Column(name = "hw_max_heap_mb")),
            @AttributeOverride(name = "avgJvmMemoryUsage", column = @Column(name = "hw_avg_jvm_memory_mb")),
            @AttributeOverride(name = "maxJvmMemoryUsage", column = @Column(name = "hw_max_jvm_memory_mb")),

    })
    private HwSummary hwSummary;

    @Column(name = "delay_ms")
    private Long delayMs;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_test_run_user"))
    private User createdBy;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = Instant.now();

        if (status == null) {
            status = TestStatus.CREATED;
        }
    }
}
