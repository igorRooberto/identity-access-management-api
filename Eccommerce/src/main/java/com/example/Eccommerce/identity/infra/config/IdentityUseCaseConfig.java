package com.example.Eccommerce.identity.infra.config;

import com.example.Eccommerce.identity.application.gateway.PasswordEncoder;
import com.example.Eccommerce.identity.application.gateway.TokenGateway;
import com.example.Eccommerce.identity.application.usecase.LoginUserUseCase;
import com.example.Eccommerce.identity.application.usecase.RefreshTokenUseCase;
import com.example.Eccommerce.identity.application.usecase.RegisterUserUseCase;
import com.example.Eccommerce.identity.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdentityUseCaseConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGateway tokenGateway;

    public IdentityUseCaseConfig(PasswordEncoder passwordEncoder, UserRepository userRepository, TokenGateway tokenGateway) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenGateway = tokenGateway;
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase() {
        return new RegisterUserUseCase(this.passwordEncoder,this.userRepository);
    }

    @Bean
    public LoginUserUseCase loginUserUseCase() {
        return new LoginUserUseCase(this.passwordEncoder,this.userRepository, this.tokenGateway);
    }

    @Bean
    public RefreshTokenUseCase  refreshTokenUseCase(){
        return new RefreshTokenUseCase(this.tokenGateway,this.userRepository);
    }
}
