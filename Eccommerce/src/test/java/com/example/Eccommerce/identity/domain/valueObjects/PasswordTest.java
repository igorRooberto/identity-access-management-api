package com.example.Eccommerce.identity.domain.valueObjects;

import com.example.Eccommerce.identity.domain.model.valueObjects.Password;
import com.example.Eccommerce.shared.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordTest {

    @Test
    @DisplayName("Deve aceitar senhas válidas")
    void shouldAcceptValidPasswords() {
        assertDoesNotThrow(() -> Password.validate("SenhaForte1!"));
    }

    @Test
    @DisplayName("Deve rejeitar senhas inválidas")
    void shouldRejectInvalidPasswords() {
        assertThrows(DomainException.class, () -> Password.validate("Curt1!"));
        assertThrows(DomainException.class, () -> Password.validate("fraca@123"));
        assertThrows(DomainException.class, () -> Password.validate("SemEspecial123"));
    }

    @Test
    @DisplayName("Construtor deve aceitar Hash (mesmo que pareça senha fraca)")
    void shouldAcceptHashInConstructor() {
        assertDoesNotThrow(() -> new Password("hash_criptografado_bizarro_123"));
        assertDoesNotThrow(() -> new Password("encodedPass"));
    }

    @Test
    @DisplayName("Construtor não deve aceitar Hash nulo ou vazio")
    void shouldRejectEmptyHash() {
        assertThrows(DomainException.class, () -> new Password(null));
        assertThrows(DomainException.class, () -> new Password(""));
    }
}
