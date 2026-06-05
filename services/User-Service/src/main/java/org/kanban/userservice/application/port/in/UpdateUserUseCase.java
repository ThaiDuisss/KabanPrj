package org.kanban.userservice.application.port.in;

import org.kanban.userservice.application.dto.UpdateUserCommand;
import org.kanban.userservice.application.dto.UserResponse;

public interface UpdateUserUseCase {
    UserResponse updateProfile(Long id, UpdateUserCommand command);
}
