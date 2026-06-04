package org.kanban.userservice.application.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateUserCommand {
    String fullName;
    String avatarUrl;
}
