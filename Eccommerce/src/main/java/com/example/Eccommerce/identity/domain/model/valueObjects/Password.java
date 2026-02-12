package com.example.Eccommerce.identity.domain.model.valueObjects;

import com.example.Eccommerce.shared.domain.exception.DomainException;

import java.util.regex.Pattern;

public record Password(String value) {

    private static final String REGEX = "^(?=.*[A-Z])(?=.*[!@#\\$%\\^&\\*]).{8,}$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public Password {

        if (value == null || value.isBlank()) {
            throw DomainException.validationError("A senha (hash) é obrigatória.");
        }
    }

    public static void validate(String rawPassword) {
        if (rawPassword == null || !PATTERN.matcher(rawPassword).matches()) {
            throw DomainException.validationError(
                    "A senha deve ter no mínimo 8 caracteres, uma letra maiúscula e um caractere especial."
            );
        }
    }
}

