package cz.uhk.loadtesterapp.service;


import cz.uhk.loadtesterapp.model.RequestDefinition;
import cz.uhk.loadtesterapp.model.TestDefinition;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ApiRequestService {

    private final WebClient webClient;

    public ApiRequestService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public Mono<ResponseEntity<String>> send(TestDefinition requestDef) {
        WebClient.RequestBodySpec request = webClient
                .method(requestDef.getMethod())
                .uri(requestDef.getUrl());

        if (requestDef.getHeaders() != null) {
            requestDef.getHeaders().forEach(request::header);
        }

        WebClient.ResponseSpec spec =
                (requestDef.getMethod() == HttpMethod.GET || requestDef.getMethod() == HttpMethod.DELETE)
                        ? request.retrieve()
                        : request
                        .contentType(MediaType.parseMediaType(
                                requestDef.getContentType() != null ? requestDef.getContentType() : "application/json"))
                        .bodyValue(requestDef.getBody() != null ? requestDef.getBody() : "")
                        .retrieve();

        return spec.toEntity(String.class);
    }

}
