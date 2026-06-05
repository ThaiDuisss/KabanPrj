package org.kanban.userservice.domain.model;

import lombok.Value;
import org.kanban.userservice.domain.exception.UserDomainException;

@Value
public class Username {
    String value;

    public Username(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new UserDomainException("Username cannot be empty");
        }
        String trimmed = value.trim();
        if (trimmed.length() < 3 || trimmed.length() > 100) {
            throw new UserDomainException("Username must be between 3 and 100 characters");
        }
        this.value = trimmed;
    }
}
