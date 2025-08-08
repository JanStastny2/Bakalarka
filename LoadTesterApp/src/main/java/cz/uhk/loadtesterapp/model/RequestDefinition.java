package cz.uhk.loadtesterapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDefinition {
    private String url;
    private HttpMethod method;
    private Map<String, String> headers;
    private String body; // raw JSON nebo XML
    private String contentType; // např. "application/json"




}
