package org.kanban.boardservice.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDescription {
    private String value;

    public BoardDescription(String value) {
        if (value != null && value.length() > 1000) {
            throw new IllegalArgumentException("Board description must be at most 1000 characters");
        }
        this.value = value == null || value.isBlank() ? null : value.trim();
    }
}
