package com.example.Eccommerce.shared.infra.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Eccommerce.identity.domain.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityFilterTest {

    @Mock private TokenVerifier tokenVerifier;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;
    @Mock private DecodedJWT decodedJWT;

    @InjectMocks
    private SecurityFilter securityFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldAuthenticate_whenTokenIsValid() throws Exception {

        String token = "tokenValid123";
        String login = "Igor";
        String userRole = "CLIENT";
        String validUUID = UUID.randomUUID().toString();
        String nowIso = Instant.now().toString();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        when(tokenVerifier.validateToken(anyString())).thenReturn(Optional.of(decodedJWT));

        Claim roleClaim = mock(Claim.class);
        Claim idClaim = mock(Claim.class);
        Claim activeClaim = mock(Claim.class);
        Claim createdAtClaim = mock(Claim.class);

        when(decodedJWT.getSubject()).thenReturn(login);

        when(decodedJWT.getClaim("ROLE")).thenReturn(roleClaim);
        when(roleClaim.isMissing()).thenReturn(false);
        when(roleClaim.isNull()).thenReturn(false);
        when(roleClaim.asString()).thenReturn(userRole);

        when(decodedJWT.getClaim("id")).thenReturn(idClaim);
        when(idClaim.isMissing()).thenReturn(false);
        when(idClaim.isNull()).thenReturn(false);
        when(idClaim.asString()).thenReturn(validUUID);

        when(decodedJWT.getClaim("active")).thenReturn(activeClaim);
        when(activeClaim.asBoolean()).thenReturn(true);

        lenient().when(activeClaim.isMissing()).thenReturn(false);
        lenient().when(activeClaim.isNull()).thenReturn(false);

        when(decodedJWT.getClaim("createdAt")).thenReturn(createdAtClaim);
        when(createdAtClaim.asString()).thenReturn(nowIso);

        securityFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication, "A autenticação falhou. O token foi rejeitado pelos filtros.");

        Object principal = authentication.getPrincipal();
        assertTrue(principal instanceof User, "O principal deve ser do tipo User");

        User user = (User) principal;
        assertEquals(login, user.getLogin());
        assertEquals(UUID.fromString(validUUID), user.getId());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenTokenIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid");
        when(tokenVerifier.validateToken("invalid")).thenReturn(Optional.empty());

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
