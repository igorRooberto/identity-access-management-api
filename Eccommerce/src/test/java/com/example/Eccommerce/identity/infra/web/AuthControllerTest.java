package com.example.Eccommerce.identity.infra.web;

import com.example.Eccommerce.identity.application.dto.loginUserUseCaseDtos.LoginInputDto;
import com.example.Eccommerce.identity.application.dto.loginUserUseCaseDtos.LoginOutputDto;
import com.example.Eccommerce.identity.application.dto.refreshTokenUseCaseDtos.RefreshTokenInput;
import com.example.Eccommerce.identity.application.dto.refreshTokenUseCaseDtos.RefreshTokenOutput;
import com.example.Eccommerce.identity.application.dto.registerUseCaseDtos.RegisterInputDto;
import com.example.Eccommerce.identity.application.gateway.TokenGateway;
import com.example.Eccommerce.identity.application.usecase.LoginUserUseCase;
import com.example.Eccommerce.identity.application.usecase.RefreshTokenUseCase;
import com.example.Eccommerce.identity.application.usecase.RegisterUserUseCase;
import com.example.Eccommerce.identity.infra.persistence.repository.JpaUserRepository;
import com.example.Eccommerce.shared.domain.exception.DomainException;
import com.example.Eccommerce.shared.infra.security.TokenVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(com.fasterxml.jackson.databind.ObjectMapper.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TokenVerifier tokenVerifier;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private LoginUserUseCase loginUserUseCase;

    @MockitoBean
    private RefreshTokenUseCase refreshTokenUseCase;

    @Nested
    @DisplayName("Cenários de Sucesso")
    class SuccessScenarios {

        @Test
        @DisplayName("POST /auth/register - Deve retornar 200 OK")
        void shouldRegisterSuccessfully() throws Exception {
            var input = new RegisterInputDto("igor", "Senha123@", "igor@teste.com");
            doNothing().when(registerUserUseCase).execute(any());

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("POST /auth/login - Deve retornar tokens")
        void shouldLoginSuccessfully() throws Exception {
            var input = new LoginInputDto("igor", "Senha123@");
            var output = new LoginOutputDto("access-token", "refresh-token");
            when(loginUserUseCase.execute(any())).thenReturn(output);

            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("access-token"))
                    .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
        }

        @Test
        @DisplayName("POST /auth/refresh - Deve renovar tokens com sucesso")
        void shouldRefreshSuccessfully() throws Exception {
            var input = new RefreshTokenInput("token-valido");
            var output = new RefreshTokenOutput("novo-access", "novo-refresh");
            when(refreshTokenUseCase.execute(any())).thenReturn(output);

            mockMvc.perform(post("/auth/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("novo-access"))
                    .andExpect(jsonPath("$.refreshToken").value("novo-refresh"));
        }
    }

    @Nested
    @DisplayName("Cenários de Falha (Regras de Negócio)")
    class BusinessFailureScenarios {

        @Test
        @DisplayName("Deve retornar 422 quando Refresh Token é inválido ou expirado")
        void shouldReturnUnprocessableEntityWhenTokenInvalid() throws Exception {
            var input = new RefreshTokenInput("token-podre");
            when(refreshTokenUseCase.execute(any()))
                    .thenThrow(DomainException.validationError("Refresh Token inválido ou expirado"));

            mockMvc.perform(post("/auth/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isUnprocessableEntity()); // 422 definido no DomainException
        }

        @Test
        @DisplayName("Deve retornar 403 quando usuário está inativo no refresh")
        void shouldReturnForbiddenWhenUserInactive() throws Exception {
            var input = new RefreshTokenInput("token-valido");
            when(refreshTokenUseCase.execute(any()))
                    .thenThrow(DomainException.AccessDenied("Usuário inativo ou bloqueado"));

            mockMvc.perform(post("/auth/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Deve retornar 409 quando o login já existe no registro")
        void shouldReturnConflictWhenEmailExist() throws Exception {
            var input = new RegisterInputDto("igor", "Senha123@", "emailJáExiste@gmail.com");
            doThrow(DomainException.conflict("Email Já existe!")).when(registerUserUseCase).execute(any());

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Deve retornar 409 quando o login já existe no registro")
        void shouldReturnConflictWhenLoginExist() throws Exception {
            var input = new RegisterInputDto("ja_existe", "Senha123@", "email@teste.com");
            doThrow(DomainException.conflict("Login Já existe!")).when(registerUserUseCase).execute(any());

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("Cenários de Falha (Validação de DTO)")
    class ValidationFailureScenarios {

        @Test
        @DisplayName("Deve retornar 400 Bad Request ao enviar campos em branco")
        void shouldReturnBadRequest() throws Exception {
            var inputInvalido = new RegisterInputDto("", "", "");

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputInvalido)))
                    .andExpect(status().isBadRequest());
        }
    }
}
