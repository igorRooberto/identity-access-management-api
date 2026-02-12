package com.example.Eccommerce.identity.application.usecases;

import com.example.Eccommerce.identity.application.dto.registerUseCaseDtos.RegisterInputDto;
import com.example.Eccommerce.identity.application.gateway.PasswordEncoder;
import com.example.Eccommerce.identity.application.usecase.RegisterUserUseCase;
import com.example.Eccommerce.identity.domain.model.User;
import com.example.Eccommerce.identity.domain.repository.UserRepository;
import com.example.Eccommerce.shared.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Test
    @DisplayName("Deve Registrar um Usuário com Sucesso")
    void shouldRegisterUserSuccessfully() {

        RegisterInputDto inputDto = new RegisterInputDto("loginTeste", "Senha123@@", "emailTeste@gmail.com");

        when(userRepository.existsByLogin("loginTeste")).thenReturn(false);
        when(userRepository.existsByEmail("emailTeste@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("Senha123@@")).thenReturn("encodedPassword");

        registerUserUseCase.execute(inputDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar erro se o Login já existe")
    void shouldThrowExceptionIfLoginExists() {

        RegisterInputDto inputDto = new RegisterInputDto("loginTeste", "Senha123@@", "emailTeste@gmail.com");

        when(userRepository.existsByLogin("loginTeste")).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class, () -> {
            registerUserUseCase.execute(inputDto);
        });

        assertEquals("Login Já existe!", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar erro se o Email já existe")
    void shouldThrowExceptionIfEmailExists() {

        RegisterInputDto inputDto = new RegisterInputDto("loginTeste", "Senha123@@", "emailTeste@gmail.com");

        when(userRepository.existsByLogin("loginTeste")).thenReturn(false);
        when(userRepository.existsByEmail("emailTeste@gmail.com")).thenReturn(true);

        DomainException exception = assertThrows(DomainException.class, () -> {
            registerUserUseCase.execute(inputDto);
        });

        assertEquals("Email já existe!", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }
}
