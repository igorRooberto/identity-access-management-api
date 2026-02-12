package com.example.Eccommerce.identity.domain.model;

import com.example.Eccommerce.shared.domain.enums.UserRole;
import com.example.Eccommerce.identity.domain.model.valueObjects.Email;
import com.example.Eccommerce.identity.domain.model.valueObjects.Password;
import com.example.Eccommerce.shared.domain.validation.Assertion;
import java.time.Instant;
import java.util.UUID;

public class User {

    private final UUID id;
    private String login;
    private Email email;
    private Password password;
    private boolean active;
    private UserRole role;
    private final Instant createdAt;
    private Instant updatedAt;

    public User(String login, Password password, Email email) {
        Assertion.notBlank(login, "O Login é Obrigatório");
        Assertion.notNull(password, "A senha é Obrigatória");
        Assertion.notNull(email, "O email é Obrigatório");

        this.id = UUID.randomUUID();
        this.active = true;
        this.role = UserRole.CLIENT;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public User(UUID id, String login, Email email, Password password, boolean active, UserRole role, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.active = active;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //Construtor Para Usar No token, SOMENTE!
    public User(UUID id, String login, UserRole role, Instant createdAt) {
        this.id = id;
        this.login = login;
        this.role = role;
        this.createdAt = createdAt;
    }

    public void activate() {
        if (this.active) return;
        this.active = true;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        if (!this.active) return;
        this.active = false;
        this.updatedAt = Instant.now();
    }


    public Instant getCreatedAt() {
        return createdAt;
    }

    public Email getEmail() {
        return email;
    }

    public boolean isActive() {
        return active;
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Password getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
