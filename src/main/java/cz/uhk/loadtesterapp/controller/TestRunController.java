package cz.uhk.loadtesterapp.controller;

import cz.uhk.loadtesterapp.model.TestDefinition;
import cz.uhk.loadtesterapp.service.ApiRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/tests")
public class TestRunController {

    private final ApiRequestService apiRequestService;

    private static final Logger log = LoggerFactory.getLogger(TestRunController.class);

    public TestRunController(ApiRequestService apiRequestService) {
        this.apiRequestService = apiRequestService;
    }

    @PostMapping("/run")
    public ResponseEntity<String> runTest(@RequestBody TestDefinition req) {
        log.info("Starting test: {} requests to {}", req.getTotalRequests(), req.getUrl());

        for (int i = 1; i <= req.getTotalRequests(); i++) {
            final int requestIndex = i;
            apiRequestService.send(req)
                    .subscribe(resp -> log.info("[{}/{}] status={} body={}",
                            requestIndex, req.getTotalRequests(),
                            resp.getStatusCodeValue(),
                            resp.getBody()
                    ));
        }

        return ResponseEntity.ok("Test started with " + req.getTotalRequests() + " requests");
    }
}
