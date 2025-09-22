package cz.uhk.loadtesterapp.service;

import org.springframework.stereotype.Service;
import reactor.core.Disposable;

import java.net.URI;
import java.time.Duration;

public interface HwSampleService {

    Disposable start(Long id, URI actuatorBase, Duration interval);

    void stopAndSummarize(Long testId);
}
