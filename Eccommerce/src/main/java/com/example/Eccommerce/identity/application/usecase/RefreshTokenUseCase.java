package com.example.Eccommerce.identity.application.usecase;

import com.example.Eccommerce.identity.application.dto.refreshTokenUseCaseDtos.RefreshTokenInput;
import com.example.Eccommerce.identity.application.dto.refreshTokenUseCaseDtos.RefreshTokenOutput;
import com.example.Eccommerce.identity.application.gateway.TokenGateway;
import com.example.Eccommerce.identity.domain.model.User;
import com.example.Eccommerce.identity.domain.repository.UserRepository;
import com.example.Eccommerce.shared.domain.exception.DomainException;

public class RefreshTokenUseCase {

    private final TokenGateway tokenGateway;
    private final UserRepository userRepository;

    public RefreshTokenUseCase(TokenGateway tokenGateway, UserRepository userRepository) {
        this.tokenGateway = tokenGateway;
        this.userRepository = userRepository;
    }

    public RefreshTokenOutput execute(RefreshTokenInput input){
        String login = tokenGateway.validateRefreshToken(input.refreshToken())
                .orElseThrow(() -> DomainException.validationError("Refresh Token inválido ou expirado"));

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> DomainException.notFound("Usuário não encontrado"));

        if (!user.isActive()) {
            throw DomainException.AccessDenied("Usuário inativo ou bloqueado, Contate o Suporte");
        }

        String newAccessToken = tokenGateway.generateToken(user);
        String newRefreshToken = tokenGateway.generateRefreshToken(user);

        return new RefreshTokenOutput(newAccessToken, newRefreshToken);
    }
}
