package com.example.Eccommerce.shared.application.dto;

import com.example.Eccommerce.shared.domain.enums.UserRole;

import java.time.Instant;
import java.util.UUID;

public record AuthenticatedUser(UUID id, String login, UserRole role, Instant createdAt) {
}
