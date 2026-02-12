package com.example.Eccommerce.identity.infra.persistence.repository;

import com.example.Eccommerce.shared.domain.enums.UserRole;
import com.example.Eccommerce.identity.infra.persistence.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class JpaUserRepositoryTest {

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity createValidUserEntity(String login, String email) {
        return new UserEntity(
                UUID.randomUUID(),
                login,
                email,
                "hashedPassword",
                true,
                UserRole.CLIENT,
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    @DisplayName("Deve retornar True se o Login existir")
    void shouldReturnTrueIfLoginExists() {

        UserEntity user = createValidUserEntity("igor", "igor@teste.com");
        entityManager.persist(user);

        boolean exists = jpaUserRepository.existsByLogin("igor");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Deve retornar False se o Login não existir")
    void shouldReturnFalseIfLoginDoesNotExist() {

        boolean exists = jpaUserRepository.existsByLogin("naoExiste");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Deve retornar True se o Email existir")
    void shouldReturnTrueIfEmailExists() {

        UserEntity user = createValidUserEntity("maria", "maria@teste.com");
        entityManager.persist(user);

        boolean exists = jpaUserRepository.existsByEmail("maria@teste.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Deve retornar False se o Email não existir")
    void shouldReturnFalseIfEmailDoesNotExist() {

        boolean exists = jpaUserRepository.existsByEmail("vazio@teste.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Deve buscar usuário por Login OU Email (usando Login)")
    void shouldFindUserByLoginOrEmail_UsingLogin() {

        UserEntity user = createValidUserEntity("joao", "joao@teste.com");
        entityManager.persist(user);

        Optional<UserEntity> result = jpaUserRepository.findByLoginOrEmail("joao");

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Deve buscar usuário por Login OU Email (usando Email)")
    void shouldFindUserByLoginOrEmail_UsingEmail() {
        UserEntity user = createValidUserEntity("ana", "ana@teste.com");
        entityManager.persist(user);

        Optional<UserEntity> result = jpaUserRepository.findByLoginOrEmail("ana@teste.com");

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por Login ou Email inexistente")
    void shouldReturnEmptyWhenUserNotFoundByLoginOrEmail() {

        Optional<UserEntity> result = jpaUserRepository.findByLoginOrEmail("inexistente");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Deve buscar usuário apenas pelo Login")
    void shouldFindUserByLogin() {

        UserEntity user = createValidUserEntity("pedro", "pedro@teste.com");
        entityManager.persist(user);

        Optional<UserEntity> result = jpaUserRepository.findByLogin("pedro");

        assertTrue(result.isPresent());
        assertEquals("pedro", result.get().getLogin());
    }

    @Test
    @DisplayName("Deve retornar vazio quando User pelo findByLogin não existir")
    void shouldReturnEmptyByLogin() {

        Optional<UserEntity> result = jpaUserRepository.findByLogin("pedro");

        assertTrue(result.isEmpty());

    }
}
