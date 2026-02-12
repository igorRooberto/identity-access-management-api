package com.example.Eccommerce.shared.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenVerifierTest {

    private TokenVerifier tokenVerifier;
    private final String SECRET = "segredo-teste";
    private final String ISSUER = "Ecommerce";

    @BeforeEach
    void setUp() {
        tokenVerifier = new TokenVerifier();

        ReflectionTestUtils.setField(tokenVerifier, "secret", SECRET);
    }

    @Test
    void validateToken_shouldReturnDecodedJWT_whenTokenIsValid() {
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withSubject("user@test.com")
                .sign(Algorithm.HMAC256(SECRET));

        Optional<DecodedJWT> result = tokenVerifier.validateToken(token);

        assertTrue(result.isPresent());
        assertEquals("user@test.com", result.get().getSubject());
    }

    @Test
    void validateToken_shouldReturnEmpty_whenIssuerIsWrong() {
        String token = JWT.create()
                .withIssuer("OutroIssuer")
                .sign(Algorithm.HMAC256(SECRET));

        assertTrue(tokenVerifier.validateToken(token).isEmpty());
    }

    @Test
    void validateToken_shouldReturnEmpty_whenTokenIsInvalid() {
        assertTrue(tokenVerifier.validateToken("token.invalido.123").isEmpty());
    }
}
