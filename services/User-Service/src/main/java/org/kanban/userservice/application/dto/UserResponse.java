package org.kanban.userservice.application.dto;

import lombok.Builder;
import lombok.Value;
import org.kanban.userservice.domain.model.User;

import java.time.LocalDateTime;

@Value
@Builder
public class UserResponse {
    Long id;
    String username;
    String email;
    String fullName;
    String avatarUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static UserResponse fromDomain(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId() != null ? user.getId().getValue() : null)
                .username(user.getUsername() != null ? user.getUsername().getValue() : null)
                .email(user.getEmail() != null ? user.getEmail().getValue() : null)
                .fullName(user.getFullName() != null ? user.getFullName().getValue() : null)
                .avatarUrl(user.getAvatarUrl() != null ? user.getAvatarUrl().getValue() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
