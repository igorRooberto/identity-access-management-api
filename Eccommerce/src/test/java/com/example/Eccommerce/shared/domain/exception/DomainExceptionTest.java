package com.example.Eccommerce.shared.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DomainExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectStatus() {
        assertEquals(422, DomainException.validationError("Error").getStatus());
        assertEquals(404, DomainException.notFound("Error").getStatus());
        assertEquals(409, DomainException.conflict("Error").getStatus());
        assertEquals(403, DomainException.AccessDenied("Error").getStatus());
    }
}
