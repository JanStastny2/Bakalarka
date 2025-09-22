package cz.uhk.loadtesterapp.service;

import cz.uhk.loadtesterapp.model.entity.TestRun;
import cz.uhk.loadtesterapp.model.enums.TestStatus;
import cz.uhk.loadtesterapp.model.dto.TestUpdateRequest;
import cz.uhk.loadtesterapp.repository.TestRepository;
import cz.uhk.loadtesterapp.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestCommandServiceImpl implements TestCommandService {

    private final TestRepository repo;
    private final UserService userService;

    @Override
    @Transactional
    public TestRun create(TestRun test, Authentication auth) {
        var principal = (MyUserDetails) auth.getPrincipal();
        test.setCreatedBy(userService.getUserById(principal.getUserId()));
        test.setStatus(TestStatus.CREATED);
        test.setCreatedAt(Instant.now());
        return repo.save(test);
    }

    @Override
    @Transactional
    public Optional<TestRun> update(Long id, TestUpdateRequest update) {
        return repo.findById(id).map(entity -> {
            if(entity.getStatus() != TestStatus.CREATED)
                throw new IllegalStateException("Test is not in CREATED state");
            entity.setTotalRequests(update.totalRequests());
            entity.setConcurrency(update.concurrency());
            entity.setProcessingMode(update.processingMode());

            if (update.poolSizeOrCap() != null)
                entity.setPoolSizeOrCap(update.poolSizeOrCap());

            if (update.delayMs() != null)
                entity.setDelayMs(update.delayMs());

            entity.setRequest(update.request());
            return entity;
        });
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public Optional<TestRun> approve(Long id) {
        return repo.findById(id).map(entity -> {
            if (entity.getStatus() != TestStatus.CREATED)
                throw new IllegalStateException("Only CREATED tests can be approved.");
            entity.setStatus(TestStatus.APPROVED);
            return entity;
        });
    }

    @Override
    @Transactional
    public Optional<TestRun> reject(Long id) {
        return repo.findById(id).map(entity -> {
            if (entity.getStatus() != TestStatus.CREATED) {
                throw new IllegalStateException("Only CREATED tests can be rejected.");
            }
            entity.setStatus(TestStatus.REJECTED);
            return entity;
        });
    }

    @Override
    @Transactional
    public Optional<TestRun> cancel(Long id) {
        return repo.findById(id).map(entity -> {
            if (entity.getStatus() != TestStatus.CREATED) {
                throw new IllegalStateException("Only CREATED tests can be canceled.");
            }
            entity.setStatus(TestStatus.CANCELLED);
            entity.setFinishedAt(Instant.now());
            return entity;
        });
    }
}
