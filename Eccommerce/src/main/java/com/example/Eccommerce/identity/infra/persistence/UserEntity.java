package com.example.Eccommerce.identity.infra.persistence;

import com.example.Eccommerce.identity.domain.model.User;
import com.example.Eccommerce.shared.domain.enums.UserRole;
import com.example.Eccommerce.identity.domain.model.valueObjects.Email;
import com.example.Eccommerce.identity.domain.model.valueObjects.Password;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_At", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "update_At", nullable = false)
    private Instant updateAt;

    public static UserEntity fromDomain(User user) {
        return new UserEntity(
                user.getId(),
                user.getLogin(),
                user.getEmail().value(),
                user.getPassword().value(),
                user.isActive(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                new Email(userEntity.getEmail()),
                new Password(userEntity.getPassword()),
                userEntity.isActive(),
                userEntity.getRole(),
                userEntity.getCreatedAt(),
                userEntity.getUpdateAt()
        );
    }

}
