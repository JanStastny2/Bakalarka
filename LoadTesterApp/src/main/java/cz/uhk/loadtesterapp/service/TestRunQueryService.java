package cz.uhk.loadtesterapp.service;

import cz.uhk.loadtesterapp.model.dto.HwSampleDto;
import cz.uhk.loadtesterapp.model.entity.TestRun;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TestRunQueryService {

    List<TestRun> list(Authentication auth);

    List<TestRun> findAllById(List<Long> ids);

    Optional<TestRun> findById(Long id);

    List<HwSampleDto> getHwSamples(Long testRunId, Instant from, Instant to);

}
