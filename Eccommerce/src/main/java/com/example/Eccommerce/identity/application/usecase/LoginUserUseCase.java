package com.example.Eccommerce.identity.application.usecase;

import com.example.Eccommerce.identity.application.dto.loginUserUseCaseDtos.LoginInputDto;
import com.example.Eccommerce.identity.application.dto.loginUserUseCaseDtos.LoginOutputDto;
import com.example.Eccommerce.identity.application.gateway.PasswordEncoder;
import com.example.Eccommerce.identity.application.gateway.TokenGateway;
import com.example.Eccommerce.identity.domain.repository.UserRepository;
import com.example.Eccommerce.shared.domain.exception.DomainException;

public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGateway tokenGateway;

    public LoginUserUseCase(PasswordEncoder passwordEncoder, UserRepository userRepository, TokenGateway tokenGateway) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenGateway = tokenGateway;
    }

    public LoginOutputDto execute(LoginInputDto input){
        var user = userRepository.findByLoginOrEmail(input.identifier())
                .orElseThrow(() -> DomainException.notFound("Usuário Não Encontrado"));

        if(!passwordEncoder.matches(input.password(), user.getPassword().value())){
            throw DomainException.AccessDenied("Senha inválida");
        }

        if(!user.isActive()){
            throw DomainException.AccessDenied("Conta Desativada, Consulte o Suporte");
        }

        var accessToken = tokenGateway.generateToken(user);
        var refreshToken = tokenGateway.generateRefreshToken(user);

        return new LoginOutputDto(accessToken, refreshToken);
    }

}
