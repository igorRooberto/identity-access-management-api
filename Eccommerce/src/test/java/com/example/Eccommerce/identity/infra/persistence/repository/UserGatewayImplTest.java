package com.example.Eccommerce.identity.infra.persistence.repository;

import com.example.Eccommerce.identity.domain.model.User;
import com.example.Eccommerce.shared.domain.enums.UserRole;
import com.example.Eccommerce.identity.domain.model.valueObjects.Email;
import com.example.Eccommerce.identity.domain.model.valueObjects.Password;
import com.example.Eccommerce.identity.infra.persistence.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserGatewayImplTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    @InjectMocks
    private UserGatewayImpl userGateway;

    private UserEntity createEntity(String login, String email) {
        return new UserEntity(
                UUID.randomUUID(), login, email, "hash",
                true, UserRole.CLIENT, Instant.now(), Instant.now()
        );
    }

    private User createDomain(String login, String email) {
        return new User(login, new Password("hash"), new Email(email));
    }

    @Test
    @DisplayName("Deve salvar um usuário com sucesso e retornar o domínio")
    void shouldSaveUserSuccessfully() {

        User userDomain = createDomain("igor", "igor@teste.com");
        UserEntity userEntity = createEntity("igor", "igor@teste.com");

        when(jpaUserRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User result = userGateway.save(userDomain);

        assertNotNull(result);
        assertEquals(userEntity.getId(), result.getId());
        assertEquals(userEntity.getLogin(), result.getLogin());
        assertEquals(userEntity.getEmail(), result.getEmail().value());

        verify(jpaUserRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve retornar true se o login existir")
    void shouldReturnTrueIfLoginExists() {

        String login = "igor";
        when(jpaUserRepository.existsByLogin(login)).thenReturn(true);

        boolean exists = userGateway.existsByLogin(login);

        assertTrue(exists);
        verify(jpaUserRepository, times(1)).existsByLogin(login);
    }

    @Test
    @DisplayName("Deve retornar false se o login não existir")
    void shouldReturnFalseIfLoginDoesNotExist() {

        String login = "NOT_EXIST";
        when(jpaUserRepository.existsByLogin(login)).thenReturn(false);

        boolean exists = userGateway.existsByLogin(login);

        assertFalse(exists);
        verify(jpaUserRepository, times(1)).existsByLogin(login);
    }

    @Test
    @DisplayName("Deve retornar true se o email existir")
    void shouldReturnTrueIfEmailExists() {
        String email = "igor@teste.com";
        when(jpaUserRepository.existsByEmail(email)).thenReturn(true);

        boolean exists = userGateway.existsByEmail(email);

        assertTrue(exists);
        verify(jpaUserRepository, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("Deve retornar false se o email não existir")
    void shouldReturnFalseIfEmailExists() {
        String email = "igor@teste.com";
        when(jpaUserRepository.existsByEmail(email)).thenReturn(false);

        boolean exists = userGateway.existsByEmail(email);

        assertFalse(exists);
        verify(jpaUserRepository, times(1)).existsByEmail(email);
    }


    @Test
    @DisplayName("Deve encontrar usuário por Login ou Email (Sucesso)")
    void shouldFindUserByLoginOrEmail() {

        String identifier = "igor";
        UserEntity entity = createEntity("igor", "igor@teste.com");

        when(jpaUserRepository.findByLoginOrEmail(identifier)).thenReturn(Optional.of(entity));

        Optional<User> result = userGateway.findByLoginOrEmail(identifier);

        assertTrue(result.isPresent());
        assertEquals(entity.getId(), result.get().getId());
        assertEquals(entity.getLogin(), result.get().getLogin());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por Login ou Email inexistente")
    void shouldReturnEmptyWhenUserNotFoundByLoginOrEmail() {
        String identifier = "inexistente";
        when(jpaUserRepository.findByLoginOrEmail(identifier)).thenReturn(Optional.empty());

        Optional<User> result = userGateway.findByLoginOrEmail(identifier);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve encontrar usuário apenas por Login (Sucesso)")
    void shouldFindUserByLogin() {

        String login = "igor";
        UserEntity entity = createEntity("igor", "igor@teste.com");

        when(jpaUserRepository.findByLogin(login)).thenReturn(Optional.of(entity));


        Optional<User> result = userGateway.findByLogin(login);


        assertTrue(result.isPresent());
        assertEquals(entity.getLogin(), result.get().getLogin());
    }

    @Test
    @DisplayName("Deve retornar vazio apenas por Login (Sucesso)")
    void shouldReturnEmptyByLogin() {

        String login = "igor";
        UserEntity entity = createEntity("igor", "igor@teste.com");

        when(jpaUserRepository.findByLogin(login)).thenReturn(Optional.empty());

        Optional<User> result = userGateway.findByLogin(login);

        assertTrue(result.isEmpty());
    }
}
