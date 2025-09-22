package cz.uhk.loadtesterapp.model.dto;

public record ChangePasswordRequest(@jakarta.validation.constraints.NotBlank String newPassword,
                                    @jakarta.validation.constraints.NotBlank String oldPassword) {
}

