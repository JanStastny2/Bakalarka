package cz.uhk.loadtesterapp.service;

import cz.uhk.loadtesterapp.model.entity.RequestDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApiRequestService {

    private final WebClient webClient;

    @Autowired
    public ApiRequestService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public ApiRequestService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseEntity<String>> send(RequestDefinition req, Map<String, String> headers) {
        HttpMethod method = req.getMethod().toSpring();

        Map<String, String> merged = new HashMap<>();
        if (req.getHeaders() != null) merged.putAll(req.getHeaders());
        if (headers != null) merged.putAll(headers);
        Map<String, String> finalHeaders = Collections.unmodifiableMap(merged);

        WebClient.RequestBodySpec spec = webClient
                .method(method)
                .uri(req.getUrl());

        if (!finalHeaders.isEmpty()) {
            finalHeaders.forEach(spec::header);
        }

        boolean methodSupportsBody =
                (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH);

        if (!methodSupportsBody) {
            return spec.exchangeToMono(response -> response.toEntity(String.class));
        }

        String contentType = (req.getContentType() != null && !req.getContentType().isBlank())
                ? req.getContentType()
                : MediaType.APPLICATION_JSON_VALUE;

        String body = (req.getBody() != null) ? req.getBody() : "";

        return spec
                .contentType(MediaType.parseMediaType(contentType))
                .bodyValue(body)
                .exchangeToMono(response -> response.toEntity(String.class));
    }

    public Mono<ResponseEntity<String>> send(RequestDefinition req) {
        return send(req, null);
    }
}
