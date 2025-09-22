package cz.uhk.loadtesterapp.service;

import cz.uhk.loadtesterapp.mapper.TestMapper;
import cz.uhk.loadtesterapp.model.dto.HwSampleDto;
import cz.uhk.loadtesterapp.model.entity.TestRun;
import cz.uhk.loadtesterapp.repository.TestRepository;
import cz.uhk.loadtesterapp.repository.TestRunHwSampleRepository;
import cz.uhk.loadtesterapp.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestRunQueryServiceImpl implements TestRunQueryService {

    private final TestRepository testRepository;
    private final TestMapper testMapper;
    private final TestRunHwSampleRepository TestRunHwSampleRepository;


    @Override
    public List<TestRun> list(Authentication auth) {
        boolean admin = auth.getAuthorities().stream()
                .anyMatch(a ->a.getAuthority().equals("ROLE_ADMIN"));
        if (admin)
            return testRepository.findAll();

        var principal = (MyUserDetails) auth.getPrincipal();
        return testRepository.findByCreatedBy_Id(principal.getUserId());
    }

    @Override
    public List<TestRun> findAllById(List<Long> ids) {
        return testRepository.findAllById(ids);
    }


    @Override
    public Optional<TestRun> findById(Long id) {
        return testRepository.findById(id);
    }

    @Override
    public List<HwSampleDto> getHwSamples(Long testRunId, Instant from, Instant to) {
        return testMapper.toSampleDto(
                TestRunHwSampleRepository.findByTestRun_IdAndTsBetweenOrderByTsAsc(testRunId, from, to)
        );
    }
}
