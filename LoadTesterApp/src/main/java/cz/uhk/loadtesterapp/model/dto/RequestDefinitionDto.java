package cz.uhk.loadtesterapp.model.dto;

import cz.uhk.loadtesterapp.model.enums.HttpMethodType;

public record RequestDefinitionDto(@jakarta.validation.constraints.NotBlank String url,
                                   @jakarta.validation.constraints.NotBlank HttpMethodType method,
                                   java.util.Map<String, String> headers,
                                   String body,
                                   String contentType) {
}

