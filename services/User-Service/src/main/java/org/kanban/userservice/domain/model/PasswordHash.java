package org.kanban.userservice.domain.model;

import lombok.Value;
import org.kanban.userservice.domain.exception.UserDomainException;

@Value
public class PasswordHash {
    String value;

    public PasswordHash(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new UserDomainException("Password hash cannot be empty");
        }
        this.value = value;
    }
}
