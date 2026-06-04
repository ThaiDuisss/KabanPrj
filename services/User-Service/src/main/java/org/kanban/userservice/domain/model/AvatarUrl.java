package org.kanban.userservice.domain.model;

import lombok.Value;
import org.kanban.userservice.domain.exception.UserDomainException;

@Value
public class AvatarUrl {
    String value;

    public AvatarUrl(String value) {
        if (value != null) {
            String trimmed = value.trim();
            if (!trimmed.isEmpty() && trimmed.length() > 500) {
                throw new UserDomainException("Avatar URL cannot exceed 500 characters");
            }
            this.value = trimmed;
        } else {
            this.value = null;
        }
    }
}
