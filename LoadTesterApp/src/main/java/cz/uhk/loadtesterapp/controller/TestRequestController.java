package cz.uhk.loadtesterapp.controller;

import cz.uhk.loadtesterapp.model.RequestDefinition;
import cz.uhk.loadtesterapp.service.ApiRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestRequestController {

    private final ApiRequestService apiRequestService;

    public TestRequestController(ApiRequestService apiRequestService) {
        this.apiRequestService = apiRequestService;
    }

    @PostMapping("/send")
    public Mono<ResponseEntity<Map<String, Object>>> send(@RequestBody RequestDefinition req) {
        long start = System.nanoTime();
        return apiRequestService.send(req)
                .map(resp -> {
                    long durationMs = (System.nanoTime() - start) / 1_000_000;

                    Map<String, Object> out = new HashMap<>();
                    out.put("status", resp.getStatusCodeValue());
                    out.put("body", resp.getBody());        // ← TADY se vrací tělo GrainWeightApp
                    out.put("durationMs", durationMs);

                    // Vrátíme stejný HTTP status, jaký poslal GrainWeightApp
                    return ResponseEntity.status(resp.getStatusCode()).body(out);
                });
    }

}
