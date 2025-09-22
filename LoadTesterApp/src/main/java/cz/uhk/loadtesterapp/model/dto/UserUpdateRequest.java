package cz.uhk.loadtesterapp.model.dto;

import cz.uhk.loadtesterapp.model.enums.Role;

public record UserUpdateRequest(Long id, String username, String email, Role role) {
}

