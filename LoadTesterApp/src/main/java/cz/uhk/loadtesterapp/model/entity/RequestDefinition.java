package cz.uhk.loadtesterapp.model.entity;

import cz.uhk.loadtesterapp.model.enums.HttpMethodType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@Builder(toBuilder = true)
public class RequestDefinition {

    @Column(length = 1024, nullable = false)
    @NotBlank
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private HttpMethodType method;


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "test_run_req_header", joinColumns = @JoinColumn(name = "test_run_id"))
    @MapKeyColumn(name = "header_name")
    @Column(name = "header_value")
    @Builder.Default
    private Map<String, String> headers = new HashMap<>();

    @Column(name = "req_body")
    private String body;

    @Column(length = 128)
    private String contentType;
}
