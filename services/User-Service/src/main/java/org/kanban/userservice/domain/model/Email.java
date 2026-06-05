package org.kanban.userservice.domain.model;

import lombok.Value;
import org.kanban.userservice.domain.exception.UserDomainException;

import java.util.regex.Pattern;

@Value
public class Email {
    String value;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    public Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new UserDomainException("Email cannot be empty");
        }
        String trimmed = value.trim();
        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new UserDomainException("Invalid email format");
        }
        this.value = trimmed;
    }
}
