package com.example.Eccommerce.identity.domain.valueObjects;

import com.example.Eccommerce.identity.domain.model.valueObjects.Email;
import com.example.Eccommerce.shared.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @Test
    @DisplayName("Deve criar e-mail válido")
    void shouldCreateValidEmail() {
        assertDoesNotThrow(() -> new Email("joao@gmail.com"));

        Email email = new Email("teste.123@empresa.com.br");
        assertEquals("teste.123@empresa.com.br", email.value());
    }

    @Test
    @DisplayName("Deve rejeitar formato inválido")
    void shouldRejectInvalidFormat() {
        assertThrows(DomainException.class, () -> new Email("emailsemarroba.com"));
        assertThrows(DomainException.class, () -> new Email("usuario@"));
    }
}
