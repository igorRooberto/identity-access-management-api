package com.example.Eccommerce.identity.domain.repository;

import com.example.Eccommerce.identity.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    Optional<User> findByLoginOrEmail(String identifier);

    Optional<User> findByLogin(String login);
}
