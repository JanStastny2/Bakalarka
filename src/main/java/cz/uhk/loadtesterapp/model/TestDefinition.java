package cz.uhk.loadtesterapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDefinition {
//    private long id;
    private String url;
    private HttpMethod method;
    private Map<String, String> headers;
    private String contentType;
    private String body;
    private int totalRequests;

//    private int concurrency;

}
