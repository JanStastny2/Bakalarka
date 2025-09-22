package cz.uhk.loadtesterapp.service;

import cz.uhk.loadtesterapp.model.entity.TestRun;
import cz.uhk.loadtesterapp.model.dto.TestUpdateRequest;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface TestCommandService {

    TestRun create(TestRun testRun, Authentication auth);

    Optional<TestRun> update(Long id, TestUpdateRequest testUpdateRequest);

    boolean deleteById(Long id);

    Optional<TestRun> approve(Long id);

    Optional<TestRun> reject(Long id);

    Optional<TestRun> cancel(Long id);

}
