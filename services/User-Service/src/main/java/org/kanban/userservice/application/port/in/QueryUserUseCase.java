package org.kanban.userservice.application.port.in;

import org.kanban.userservice.application.dto.UserResponse;

import java.util.Optional;

public interface QueryUserUseCase {
    Optional<UserResponse> getUserById(Long id);
    Optional<UserResponse> getUserByEmail(String email);
    Optional<UserResponse> getUserByUsername(String username);
}
