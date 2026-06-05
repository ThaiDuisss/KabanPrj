package org.kanban.userservice.domain.model;

import lombok.Value;

@Value
public class UserId {
    Long value;

    public UserId(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        this.value = value;
    }
}
