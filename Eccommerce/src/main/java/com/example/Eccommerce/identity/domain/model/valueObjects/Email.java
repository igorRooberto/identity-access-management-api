package com.example.Eccommerce.identity.domain.model.valueObjects;

import com.example.Eccommerce.shared.domain.exception.DomainException;
import com.example.Eccommerce.shared.domain.validation.Assertion;

public record Email(String value) {

    public Email(String value){

        String cleanEmail = value.trim();

        if (!cleanEmail.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw DomainException.validationError("Email inválido: " + cleanEmail);
        }

        this.value = cleanEmail;
    }
}
