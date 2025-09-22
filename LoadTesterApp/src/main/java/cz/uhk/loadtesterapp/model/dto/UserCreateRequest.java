package cz.uhk.loadtesterapp.model.dto;

import cz.uhk.loadtesterapp.model.enums.Role;

public record UserCreateRequest(@jakarta.validation.constraints.NotBlank String username,
                                @jakarta.validation.constraints.NotBlank String password,
                                @jakarta.validation.constraints.Email String email,
                                @jakarta.validation.constraints.NotNull Role role ) {
}

