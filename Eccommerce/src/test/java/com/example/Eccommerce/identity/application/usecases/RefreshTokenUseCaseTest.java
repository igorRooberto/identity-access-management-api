package com.example.Eccommerce.identity.application.usecases;

import com.example.Eccommerce.identity.application.dto.refreshTokenUseCaseDtos.RefreshTokenInput;
import com.example.Eccommerce.identity.application.dto.refreshTokenUseCaseDtos.RefreshTokenOutput;
import com.example.Eccommerce.identity.application.gateway.TokenGateway;
import com.example.Eccommerce.identity.application.usecase.RefreshTokenUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenUseCaseTest {

    @Mock
    private TokenGateway tokenGateway;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RefreshTokenUseCase refreshTokenUseCase;

    private User createValidUser(){
        return new User(
                "igor",
                new Password("hashDaSenhaNoBanco"),
                new Email("igor@teste.com")
        );
    }

    @Test
    @DisplayName("Deve renovar os tokens com sucesso")
    void shouldRefreshTokensSuccessfully() {
        RefreshTokenInput input = new RefreshTokenInput("refresh-Token-access");
        User user = createValidUser();

        when(tokenGateway.validateRefreshToken("refresh-Token-access")).thenReturn(Optional.of("igor"));
        when(userRepository.findByLogin("igor")).thenReturn(Optional.of(user));
        when(tokenGateway.generateToken(user)).thenReturn("new-access-token");
        when(tokenGateway.generateRefreshToken(user)).thenReturn("new-refresh-token");

        RefreshTokenOutput output = refreshTokenUseCase.execute(input);

        assertNotNull(output);
        assertEquals("new-access-token", output.accessToken());
        assertEquals("new-refresh-token", output.refreshToken());

        verify(tokenGateway, times(1)).generateToken(user);
        verify(tokenGateway, times(1)).generateRefreshToken(user);
    }

    @Test
    @DisplayName("Deve falhar se o Refresh Token for inválido ou expirado")
    void shouldFailIfRefreshTokenIsInvalid() {
        RefreshTokenInput input = new RefreshTokenInput("refresh-Token-access");

        when(tokenGateway.validateRefreshToken("refresh-Token-access")).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> refreshTokenUseCase.execute(input));

        assertEquals("Refresh Token inválido ou expirado", exception.getMessage());
        assertEquals(422, exception.getStatus());

        verify(tokenGateway, never()).generateToken(any(User.class));
        verify(tokenGateway, never()).generateRefreshToken(any(User.class));
        verify(userRepository, never()).findByLogin(any(String.class));
    }

    @Test
    @DisplayName("Deve falhar se o Usuário do token não existir mais")
    void shouldFailIfUserNotFound() {
        RefreshTokenInput input = new RefreshTokenInput("refresh-Token-access");

        when(tokenGateway.validateRefreshToken("refresh-Token-access")).thenReturn(Optional.of("igor"));
        when(userRepository.findByLogin("igor")).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> refreshTokenUseCase.execute(input));

        assertEquals("Usuário não encontrado", exception.getMessage());
        assertEquals(404, exception.getStatus());

        verify(tokenGateway, never()).generateToken(any(User.class));
        verify(tokenGateway, never()).generateRefreshToken(any(User.class));
    }

    @Test
    @DisplayName("Deve falhar se o Usuário estiver inativo")
    void shouldFailIfUserIsInactive() {
        RefreshTokenInput input = new RefreshTokenInput("refresh-Token-access");
        User user = createValidUser();

        user.deactivate();

        when(tokenGateway.validateRefreshToken("refresh-Token-access")).thenReturn(Optional.of("igor"));
        when(userRepository.findByLogin("igor")).thenReturn(Optional.of(user));

        DomainException exception = assertThrows(DomainException.class, () -> refreshTokenUseCase.execute(input));

        assertEquals("Usuário inativo ou bloqueado, Contate o Suporte", exception.getMessage());
        assertEquals(403, exception.getStatus());

        verify(tokenGateway, never()).generateToken(any(User.class));
        verify(tokenGateway, never()).generateRefreshToken(any(User.class));
    }
}
