package cz.uhk.loadtesterapp.service;

import cz.uhk.loadtesterapp.model.enums.ProcessingMode;
import cz.uhk.loadtesterapp.model.entity.TestRun;
import cz.uhk.loadtesterapp.model.enums.TestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.time.Instant;

public interface TestRunnerService {

    Mono<TestRun> run(Long id);

    Page<TestRun> search(TestStatus status,
                         ProcessingMode mode,
                         Instant from,
                         Instant to,
                         Pageable pageable);
}
