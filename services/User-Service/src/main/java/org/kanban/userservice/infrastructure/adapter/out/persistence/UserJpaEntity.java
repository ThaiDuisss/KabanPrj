package org.kanban.userservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.*;
import org.kanban.userservice.domain.model.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public User toDomain() {
        return User.builder()
                .id(new UserId(this.id))
                .username(new Username(this.username))
                .email(new Email(this.email))
                .passwordHash(new PasswordHash(this.passwordHash))
                .fullName(new FullName(this.fullName))
                .avatarUrl(new AvatarUrl(this.avatarUrl))
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    public static UserJpaEntity fromDomain(User user) {
        if (user == null) {
            return null;
        }
        return UserJpaEntity.builder()
                .id(user.getId() != null ? user.getId().getValue() : null)
                .username(user.getUsername() != null ? user.getUsername().getValue() : null)
                .email(user.getEmail() != null ? user.getEmail().getValue() : null)
                .passwordHash(user.getPasswordHash() != null ? user.getPasswordHash().getValue() : null)
                .fullName(user.getFullName() != null ? user.getFullName().getValue() : null)
                .avatarUrl(user.getAvatarUrl() != null ? user.getAvatarUrl().getValue() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
