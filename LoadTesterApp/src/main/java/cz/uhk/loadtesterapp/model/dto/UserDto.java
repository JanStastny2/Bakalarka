package cz.uhk.loadtesterapp.model.dto;

import cz.uhk.loadtesterapp.model.enums.Role;

public record UserDto(Long id, String username, String email, Role role) {
}

