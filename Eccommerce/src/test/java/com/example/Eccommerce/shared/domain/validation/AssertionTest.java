package com.example.Eccommerce.shared.domain.validation;

import com.example.Eccommerce.shared.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionTest {

    @Test
    void notNull_shouldThrowDomainException_whenObjectIsNull() {
        DomainException exception = assertThrows(DomainException.class, () ->
                Assertion.notNull(null, "Objeto nulo")
        );
        assertEquals("Objeto nulo", exception.getMessage());
        assertEquals(422, exception.getStatus());
    }

    @Test
    void notNull_shouldNotThrow_whenObjectIsNotNull() {
        assertDoesNotThrow(() -> Assertion.notNull(new Object(), "Deve passar"));
    }

    @Test
    void notBlank_shouldThrowDomainException_whenStringIsBlankOrNull() {
        assertThrows(DomainException.class, () -> Assertion.notBlank(null, "String nula"));
        assertThrows(DomainException.class, () -> Assertion.notBlank("", "String vazia"));
        assertThrows(DomainException.class, () -> Assertion.notBlank("   ", "String em branco"));
    }

    @Test
    void notBlank_shouldNotThrow_whenStringIsValid() {
        assertDoesNotThrow(() -> Assertion.notBlank("valid", "Deve passar"));
    }
}
