package com.example.Eccommerce.identity.application.dto.refreshTokenUseCaseDtos;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenInput(@NotBlank String refreshToken) {
}
