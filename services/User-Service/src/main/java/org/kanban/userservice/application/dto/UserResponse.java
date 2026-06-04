package org.kanban.userservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kanban.userservice.domain.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
