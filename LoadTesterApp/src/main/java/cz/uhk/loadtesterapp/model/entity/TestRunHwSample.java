package cz.uhk.loadtesterapp.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "test_run_hw_sample")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRunHwSample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "test_run_id", foreignKey = @ForeignKey(name = "fk_hw_sample_test"))
    private TestRun testRun;

    @Column(name = "ts", nullable = false)
    private Instant ts;

    @Column(name = "cpu")
    private Double cpu;

    @Column(name = "heap_mb")
    private Double heapMb;


}
