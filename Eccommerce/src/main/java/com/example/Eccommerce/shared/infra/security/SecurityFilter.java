package com.example.Eccommerce.shared.infra.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Eccommerce.identity.domain.model.User;
import com.example.Eccommerce.shared.application.dto.AuthenticatedUser;
import com.example.Eccommerce.shared.domain.enums.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SecurityFilter extends OncePerRequestFilter {

   private final TokenVerifier tokenVerifier;

    public SecurityFilter(TokenVerifier tokenVerifier) {
        this.tokenVerifier = tokenVerifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recoverToken(request);

        if(token != null){
            authenticateClient(token);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var auth = request.getHeader("Authorization");
        if(auth == null) return null;
        return auth.replace("Bearer", "").trim();
    }

    private void authenticateClient(String token) {
        tokenVerifier.validateToken(token)
                .filter(jwt ->jwt.getSubject() != null && !jwt.getSubject().isBlank())
                .filter(jwt -> !jwt.getClaim("ROLE").isMissing())
                .filter(jwt -> !jwt.getClaim("ROLE").isNull())
                .filter(jwt -> !jwt.getClaim("id").isMissing())
                .filter(jwt -> !jwt.getClaim("id").isNull())
                .filter(jwt -> Boolean.TRUE.equals(jwt.getClaim("active").asBoolean()))
                .ifPresent(this::setSecurityContext);
    }

    private void setSecurityContext(DecodedJWT jwt) {
        String login = jwt.getSubject();
        String role = jwt.getClaim("ROLE").asString();
        Instant createdTime = Instant.parse(jwt.getClaim("createdAt").asString());
        String idString = jwt.getClaim("id").asString();
        UUID id = UUID.fromString(idString);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        if ("ADMIN".equals(role)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(id, login, UserRole.valueOf(role), createdTime);

        var auth = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}

