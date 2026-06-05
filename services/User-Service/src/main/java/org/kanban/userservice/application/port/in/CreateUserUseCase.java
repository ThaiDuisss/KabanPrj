package org.kanban.userservice.application.port.in;

import org.kanban.userservice.application.dto.RegisterUserCommand;
import org.kanban.userservice.application.dto.UserResponse;

public interface CreateUserUseCase {
    UserResponse registerUser(RegisterUserCommand command);
}
