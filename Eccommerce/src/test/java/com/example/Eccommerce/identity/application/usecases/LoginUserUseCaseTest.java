package com.example.Eccommerce.identity.application.usecases;

import com.example.Eccommerce.identity.application.dto.loginUserUseCaseDtos.LoginInputDto;
import com.example.Eccommerce.identity.application.dto.loginUserUseCaseDtos.LoginOutputDto;
import com.example.Eccommerce.identity.application.gateway.PasswordEncoder;
import com.example.Eccommerce.identity.application.gateway.TokenGateway;
import com.example.Eccommerce.identity.application.usecase.LoginUserUseCase;
import com.example.Eccommerce.identity.domain.model.User;
import com.example.Eccommerce.identity.domain.model.valueObjects.Email;
import com.example.Eccommerce.identity.domain.model.valueObjects.Password;
import com.example.Eccommerce.identity.domain.repository.UserRepository;
import com.example.Eccommerce.shared.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenGateway tokenGateway;

    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    private User createValidUser(){
        return new User(
                "igor",
                new Password("hashDaSenhaNoBanco"),
                new Email("igor@teste.com")
        );
    }

    @Test
    @DisplayName("Deve fazer Login com Sucesso")
    void shouldLoginUserSuccessfully(){

        LoginInputDto input = new LoginInputDto("igor", "Senha123@");
        User user = createValidUser();

        when(userRepository.findByLoginOrEmail("igor")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Senha123@","hashDaSenhaNoBanco")).thenReturn(true);

        when(tokenGateway.generateToken(user)).thenReturn("access-token-123");
        when(tokenGateway.generateRefreshToken(user)).thenReturn("refresh-token-123");

        LoginOutputDto output = loginUserUseCase.execute(input);

        assertNotNull(output);
        assertEquals("access-token-123", output.accessToken());
        assertEquals("refresh-token-123", output.refreshToken());
    }

    @Test
    @DisplayName("Deve falhar se o usuário não for encontrado")
    void shouldFailIfUserNotFound() {
        LoginInputDto input = new LoginInputDto("igor", "Senha123@");

        when(userRepository.findByLoginOrEmail("igor")).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> loginUserUseCase.execute(input));

        assertEquals("Usuário Não Encontrado", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    @DisplayName("Deve falhar se a senha estiver incorreta")
    void shouldFailIfPasswordDoesNotMatch() {
        LoginInputDto input = new LoginInputDto("igor", "Senha123@");
        User user = createValidUser();

        when(userRepository.findByLoginOrEmail("igor")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Senha123@","hashDaSenhaNoBanco")).thenReturn(false);

        DomainException exception = assertThrows(DomainException.class, () -> loginUserUseCase.execute(input));

        assertEquals("Senha inválida", exception.getMessage());
        assertEquals(403, exception.getStatus());
    }

    @Test
    @DisplayName("Deve falhar se o usuário estiver inativo")
    void shouldFailIfUserIsInactive() {
        LoginInputDto input = new LoginInputDto("igor", "Senha123@");
        User user = createValidUser();

        user.deactivate();

        when(userRepository.findByLoginOrEmail("igor")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Senha123@","hashDaSenhaNoBanco")).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class, () -> loginUserUseCase.execute(input));

        assertEquals("Conta Desativada, Consulte o Suporte", exception.getMessage());
        assertEquals(403, exception.getStatus());
    }
}
