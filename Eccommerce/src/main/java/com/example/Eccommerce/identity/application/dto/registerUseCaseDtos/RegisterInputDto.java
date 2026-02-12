package com.example.Eccommerce.identity.application.dto.registerUseCaseDtos;

import jakarta.validation.constraints.NotBlank;

public record RegisterInputDto(@NotBlank String login,@NotBlank String password,@NotBlank String email) {
}
