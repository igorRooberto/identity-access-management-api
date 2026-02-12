package com.example.Eccommerce.shared.infra.security;

import com.example.Eccommerce.shared.application.dto.AuthenticatedUser;
import com.example.Eccommerce.shared.application.gateway.AuthGateway;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpringSecurityUserAuthenticated implements AuthGateway {

    @Override
    public AuthenticatedUser getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser;
        }

        throw new RuntimeException("Erro Usuário não autenticado");
    }
}
