package com.example.Eccommerce.shared.application.gateway;

import com.example.Eccommerce.shared.application.dto.AuthenticatedUser;

public interface AuthGateway {

    AuthenticatedUser getCurrentUser();
}
