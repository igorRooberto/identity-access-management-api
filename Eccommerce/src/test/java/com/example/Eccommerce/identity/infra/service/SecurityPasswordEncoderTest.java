package com.example.Eccommerce.identity.infra.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityPasswordEncoderTest {

    private final BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
    private final SecurityPasswordEncoder securityPasswordEncoder = new SecurityPasswordEncoder(bCrypt);

    @Test
    @DisplayName("Deve criptografar a senha corretamente (Hash diferente do original)")
    void shouldEncodePassword() {
        String rawPassword = "SenhaSuperSecreta123";

        String hash = securityPasswordEncoder.encode(rawPassword);

        assertNotNull(hash);
        assertNotEquals(rawPassword, hash, "O hash não pode ser igual à senha em texto plano");
        assertTrue(bCrypt.matches(rawPassword, hash), "O hash gerado deve ser válido para o BCrypt");
    }

    @Test
    @DisplayName("Deve validar se a senha bate com o hash")
    void shouldMatchPassword() {
        String password = "Teste";
        String hash = bCrypt.encode(password);

        assertTrue(securityPasswordEncoder.matches(password, hash));
        assertFalse(securityPasswordEncoder.matches("SenhaErrada", hash));
    }
}
