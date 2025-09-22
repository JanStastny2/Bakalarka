//package cz.uhk.loadtesterapp;
//import cz.uhk.loadtesterapp.model.enums.HttpMethodType;
//import cz.uhk.loadtesterapp.model.entity.RequestDefinition;
//import cz.uhk.loadtesterapp.service.ApiRequestService;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.reactive.function.client.ClientResponse;
//import org.springframework.web.reactive.function.client.ExchangeFunction;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class ApiRequestServiceTest {
//
//    // Pomocný ExchangeFunction mock – vždy vrátí 200 OK s daným tělem
//    private ExchangeFunction mockExchange(String body, HttpStatus status) {
//        return request -> {
//            ClientResponse response = ClientResponse.create(status)
//                    .header(HttpHeaders.CONTENT_TYPE, "text/plain")
//                    .body(body)
//                    .build();
//            return Mono.just(response);
//        };
//    }
//
//    @Test
//    void send_getWithoutBody_returnsOk() {
//        WebClient.Builder builder = WebClient.builder()
//                .exchangeFunction(mockExchange("OK", HttpStatus.OK));
//
//        ApiRequestService service = new ApiRequestService(builder);
//
//
//
//
//        RequestDefinition req = RequestDefinition.builder()
//                .url("http://fake/test")
//                .method(HttpMethodType.GET)
//                .build();
//
//        StepVerifier.create(service.send(req))
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
//                    assertThat(resp.getBody()).isEqualTo("OK");
//                })
//                .verifyComplete();
//    }
//
//    @Test
//    void send_postWithBodyAndContentType() {
//        WebClient mockClient = WebClient.builder()
//                .exchangeFunction(mockExchange("{\"done\":true}", HttpStatus.CREATED))
//                .build();
//        ApiRequestService service = new ApiRequestService(mockClient);
//
//        RequestDefinition req = RequestDefinition.builder()
//                .url("http://fake/post")
//                .method(HttpMethodType.POST)
//                .body("{\"x\":1}")
//                .contentType("application/json")
//                .build();
//
//        StepVerifier.create(service.send(req))
//                .assertNext(resp -> {
//                    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//                    assertThat(resp.getBody()).contains("done");
//                })
//                .verifyComplete();
//    }
//
//    @Test
//    void send_mergesHeaders() {
//        WebClient mockClient = WebClient.builder()
//                .exchangeFunction(req -> {
//                    // ověříme, že hlavička byla přidána
//                    assertThat(req.headers().getFirst("X-Test")).isEqualTo("abc");
//                    ClientResponse resp = ClientResponse.create(HttpStatus.OK)
//                            .body("hdr-ok")
//                            .build();
//                    return Mono.just(resp);
//                })
//                .build();
//        ApiRequestService service = new ApiRequestService(mockClient);
//
//        RequestDefinition req = RequestDefinition.builder()
//                .url("http://fake/hdr")
//                .method(HttpMethodType.GET)
//                .headers(Map.of("X-Test", "abc"))
//                .build();
//
//        StepVerifier.create(service.send(req))
//                .assertNext(resp -> assertThat(resp.getBody()).isEqualTo("hdr-ok"))
//                .verifyComplete();
//    }
//}