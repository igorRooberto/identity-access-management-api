package com.example.Eccommerce.identity.infra.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Eccommerce.identity.domain.model.User;
import com.example.Eccommerce.identity.domain.model.valueObjects.Email;
import com.example.Eccommerce.identity.domain.model.valueObjects.Password;
import com.example.Eccommerce.shared.infra.security.TokenVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenGeneratorTest {

    @Mock
    private TokenVerifier tokenVerifier;

    @InjectMocks
    private TokenGenerator tokenGenerator;

    private final String SECRET = "my-secret-key-test";
    private User user;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(tokenGenerator, "secret", SECRET);

        user = new User("igor", new Password("PasswordHash"),new Email("igor2005@gmail.com"));
    }

    @Test
    @DisplayName("Deve gerar um Access Token válido com Claims corretas")
    void shouldGenerateValidAccessToken() {
        String token = tokenGenerator.generateToken(user);

        assertNotNull(token);

        DecodedJWT decoded = JWT.require(Algorithm.HMAC256(SECRET))
                .withIssuer("Ecommerce")
                .build()
                .verify(token);

        assertEquals("igor", decoded.getSubject());
        assertEquals("CLIENT", decoded.getClaim("ROLE").asString());
        assertNotNull(decoded.getClaim("id").asString());
        assertNotNull(decoded.getClaim("createdAt").asString());
        assertNotNull(decoded.getClaim("active").asBoolean());
    }

    @Test
    @DisplayName("Deve gerar um Refresh Token válido")
    void shouldGenerateValidRefreshToken() {

        String token = tokenGenerator.generateRefreshToken(user);

        assertNotNull(token);

        DecodedJWT decoded = JWT.require(Algorithm.HMAC256(SECRET))
                .withIssuer("Ecommerce")
                .build()
                .verify(token);

        assertEquals("igor", decoded.getSubject());
        assertNotNull(decoded.getClaim("id").asString());
    }

    @Test
    @DisplayName("Deve delegar a validação do Refresh Token para o TokenVerifier")
    void shouldDelegateRefreshTokenValidation() {

        String refreshToken = "refresh-token-valido";
        when(tokenVerifier.validateRefreshToken(refreshToken)).thenReturn(Optional.of("igor"));

        Optional<String> result = tokenGenerator.validateRefreshToken(refreshToken);

        assertTrue(result.isPresent());
        assertEquals("igor", result.get());
        verify(tokenVerifier).validateRefreshToken(refreshToken);

    }
}
