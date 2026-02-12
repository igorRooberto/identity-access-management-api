package com.example.Eccommerce.shared.infra.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Eccommerce.shared.application.dto.AuthenticatedUser;
import com.example.Eccommerce.shared.domain.enums.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpringSecurityUserAuthenticatedTest {

    private SpringSecurityUserAuthenticated springSecurityUserAuthenticated;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        springSecurityUserAuthenticated = new SpringSecurityUserAuthenticated();
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUser_shouldReturnAuthenticatedUser_whenPrincipalIsCorrect() {

        UUID validUUID = UUID.randomUUID();
        String userLogin = "Igor";
        UserRole role = UserRole.CLIENT;
        Instant createdAt = Instant.now();

        AuthenticatedUser principalDto = new AuthenticatedUser(validUUID, userLogin, role, createdAt);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principalDto);

        AuthenticatedUser result = springSecurityUserAuthenticated.getCurrentUser();

        assertNotNull(result);
        assertEquals(validUUID, result.id());
        assertEquals(userLogin, result.login());
        assertEquals(role, result.role());
        assertEquals(createdAt, result.createdAt());
    }

    @Test
    void getCurrentUser_shouldThrowException_whenNotAuthenticated() {
        when(securityContext.getAuthentication()).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            springSecurityUserAuthenticated.getCurrentUser();
        });

        assertEquals("Erro Usuário não autenticado", exception.getMessage());
    }

    @Test
    void getCurrentUser_shouldThrowException_whenPrincipalIsWrongType() {
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn("TipoErradoDeObjeto");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            springSecurityUserAuthenticated.getCurrentUser();
        });

        assertEquals("Erro Usuário não autenticado", exception.getMessage());
    }
}
