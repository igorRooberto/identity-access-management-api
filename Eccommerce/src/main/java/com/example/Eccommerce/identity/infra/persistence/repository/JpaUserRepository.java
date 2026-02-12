package com.example.Eccommerce.identity.infra.persistence.repository;

import com.example.Eccommerce.identity.infra.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("SELECT COUNT(u) > 0 FROM UserEntity u WHERE u.login = :login")
    boolean existsByLogin(@Param("login") String login);

    @Query("SELECT COUNT(u) > 0 FROM UserEntity u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT u FROM UserEntity u WHERE u.login = :identifier OR u.email = :identifier")
    Optional<UserEntity> findByLoginOrEmail(@Param("identifier") String identifier);

    @Query("SELECT u FROM UserEntity u WHERE u.login = :login")
    Optional<UserEntity> findByLogin(@Param("login") String login);
}
