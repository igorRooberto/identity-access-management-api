package com.example.Eccommerce.identity.domain.model;

import com.example.Eccommerce.shared.domain.enums.UserRole;
import com.example.Eccommerce.identity.domain.model.valueObjects.Email;
import com.example.Eccommerce.identity.domain.model.valueObjects.Password;
import com.example.Eccommerce.shared.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade User")
public class UserTest {

    final String validLogin = "joaosilva";
    final Email validEmail = new Email("joao@teste.com");
    final Password validPassword = new Password("SenhaForte1!");


    @Nested
    @DisplayName("1. Criação e Validação (Construtor)")
    class Creation {

        @Test
        @DisplayName("Sucesso: Deve criar usuário com dados válidos")
        void shouldCreateUser() {
            User user = new User(validLogin, validPassword, validEmail);

            assertAll(
                    () -> assertNotNull(user.getId(), "ID não pode ser nulo"),
                    () -> assertTrue(user.isActive(), "Deve nascer ATIVO"),
                    () -> assertNotNull(user.getCreatedAt(), "Data de criação deve existir"),
                    () -> assertEquals(validLogin, user.getLogin()),
                    () -> assertEquals(UserRole.CLIENT, user.getRole())
            );
        }

        @Test
        @DisplayName("Erro: Deve falhar se LOGIN for vazio ou nulo")
        void shouldFailInvalidLogin() {

            DomainException erroNulo = assertThrows(DomainException.class, () ->
                    new User(null, validPassword, validEmail)
            );
            assertEquals("O Login é Obrigatório", erroNulo.getMessage());

            DomainException erroVazio = assertThrows(DomainException.class, () ->
                    new User("", validPassword, validEmail)
            );
            assertEquals("O Login é Obrigatório", erroVazio.getMessage());
        }

        @Test
        @DisplayName("Erro: Deve falhar se PASSWORD for nulo")
        void shouldFailNullPassword() {

            DomainException erro = assertThrows(DomainException.class, () ->
                    new User(validLogin, null, validEmail)
            );
            assertEquals("A senha é Obrigatória", erro.getMessage());
        }

        @Test
        @DisplayName("Erro: Deve falhar se EMAIL for nulo")
        void shouldFailNullEmail() {
            DomainException erro = assertThrows(DomainException.class, () ->
                    new User(validLogin, validPassword, null)
            );
            assertEquals("O email é Obrigatório", erro.getMessage());
        }
    }

    @Nested
    @DisplayName("2. Regras de Negócio (Ativação/Desativação)")
    class BusinessRules {

        @Test
        @DisplayName("Deve desativar o usuário e atualizar o timestamp")
        void shouldDeactivateUser() throws InterruptedException {
            User user = new User(validLogin, validPassword, validEmail);
            Instant createdAt = user.getUpdatedAt();

            Thread.sleep(10);
            user.deactivate();

            assertFalse(user.isActive(), "Usuário deve estar inativo (false)");
            assertTrue(user.getUpdatedAt().isAfter(createdAt));

        }

        @Test
        @DisplayName("Deve ativar um usuário inativo e atualizar o timestamp")
        void shouldActivateUser() throws InterruptedException {
            User user = new User(validLogin, validPassword, validEmail);
            user.deactivate();
            Instant dateDeactivate = user.getUpdatedAt();

            Thread.sleep(10);
            user.activate();

            assertTrue(user.isActive(), "Usuário deve voltar a ser ativo (true)");
            assertTrue(user.getUpdatedAt().isAfter(dateDeactivate));

        }

        @Test
        @DisplayName("Não deve atualizar data se status não mudar (Idempotência)")
        void shouldNotUpdateDateIfAlreadyInState() throws InterruptedException {

            User user = new User(validLogin, validPassword, validEmail);
            Instant dataInicial = user.getUpdatedAt();

            Thread.sleep(10);
            user.activate();

            assertEquals(dataInicial, user.getUpdatedAt(),
                    "Não deve atualizar data se o usuário já estava ativo");
        }
    }

}


