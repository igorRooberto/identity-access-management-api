package com.example.Eccommerce.shared.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenVerifier {

    @Value("${api.security.secret}")
    private String secret;

    public Optional<DecodedJWT> validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return Optional.of(
                    JWT.require(algorithm)
                            .withIssuer("Ecommerce")
                            .build()
                            .verify(token)
            );
        }
        catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }

    public  Optional<String> validateRefreshToken(String refreshToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return Optional.of(
                    JWT.require(algorithm)
                            .withIssuer("Ecommerce")
                            .build()
                            .verify(refreshToken)
                            .getSubject()
            );
        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }
}
