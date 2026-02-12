package com.example.Eccommerce.identity.infra.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.Eccommerce.identity.application.gateway.TokenGateway;
import com.example.Eccommerce.identity.domain.model.User;
import com.example.Eccommerce.shared.infra.security.TokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Component
public class TokenGenerator implements TokenGateway {

    @Value("${api.security.secret}")
    private String secret;

    private final TokenVerifier tokenVerifier;

    public TokenGenerator(TokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }

    @Override
    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        try{
            return JWT.create()
                    .withIssuer("Ecommerce")
                    .withSubject(user.getLogin())
                    .withClaim("ROLE", user.getRole().name())
                    .withClaim("id",user.getId().toString())
                    .withClaim("createdAt", user.getCreatedAt().toString())
                    .withClaim("active",user.isActive())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        }
        catch (JWTCreationException ex){
            throw new JWTCreationException("ERROR WHILE GENERATING TOKEN",ex);
        }
    }

    @Override
    public String generateRefreshToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try{
            return JWT.create()
                    .withIssuer("Ecommerce")
                    .withSubject(user.getLogin())
                    .withClaim("id",user.getId().toString())
                    .withExpiresAt(genRefreshExpirationDate())
                    .sign(algorithm);
        }
        catch (JWTCreationException ex){
            throw new JWTCreationException("ERROR WHILE GENERATING REFRESH TOKEN",ex);
        }
    }

    @Override
    public Optional<String> validateRefreshToken(String refreshToken) {
        return tokenVerifier.validateRefreshToken(refreshToken);
    }

    private Instant genExpirationDate(){
        return Instant.now().plus(15, ChronoUnit.MINUTES);
    }

    private Instant genRefreshExpirationDate(){
        return Instant.now().plus(64, ChronoUnit.HOURS);
    }

}
