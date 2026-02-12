package com.example.Eccommerce.identity.application.gateway;

import com.example.Eccommerce.identity.domain.model.User;

import java.util.Optional;

public interface TokenGateway {

    String generateToken(User user);
    String generateRefreshToken(User user);
    Optional<String> validateRefreshToken(String refreshToken);
}
