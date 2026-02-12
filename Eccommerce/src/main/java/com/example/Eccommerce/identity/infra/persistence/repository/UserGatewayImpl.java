package com.example.Eccommerce.identity.infra.persistence.repository;

import com.example.Eccommerce.identity.domain.model.User;
import com.example.Eccommerce.identity.domain.repository.UserRepository;
import com.example.Eccommerce.identity.infra.persistence.UserEntity;
import com.example.Eccommerce.shared.domain.exception.DomainException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserGatewayImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserGatewayImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntity.fromDomain(user);

        UserEntity userSaved = jpaUserRepository.save(userEntity);

        return UserEntity.toDomain(userSaved);
    }

    @Override
    public boolean existsByLogin(String login) {
        return jpaUserRepository.existsByLogin(login);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByLoginOrEmail(String identifier) {
        Optional<UserEntity> userEntity = jpaUserRepository.findByLoginOrEmail(identifier);

         return userEntity.map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        Optional<UserEntity> userEntity = jpaUserRepository.findByLogin(login);

        return userEntity.map(UserEntity::toDomain);
    }
}
