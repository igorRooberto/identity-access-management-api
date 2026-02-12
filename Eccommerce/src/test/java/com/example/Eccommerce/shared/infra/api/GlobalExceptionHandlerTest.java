package com.example.Eccommerce.shared.infra.api;

import com.example.Eccommerce.shared.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleDomainException_shouldReturnStatusAndBody() {
        DomainException ex = new DomainException("Mensagem de erro", 418); // I'm a teapot

        ResponseEntity<String> response = handler.handleDomainException(ex);

        assertEquals(418, response.getStatusCode().value());
        assertEquals("Mensagem de erro", response.getBody());
    }
}
