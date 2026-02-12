package com.example.Eccommerce.identity.application.dto.loginUserUseCaseDtos;

import jakarta.validation.constraints.NotBlank;

public record LoginInputDto(@NotBlank String identifier,@NotBlank String password) {
}
