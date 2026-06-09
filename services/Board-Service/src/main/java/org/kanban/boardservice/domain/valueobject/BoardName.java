package org.kanban.boardservice.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardName {
    private String value;

    public BoardName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Board name must not be blank");
        }
        if (value.length() > 255) {
            throw new IllegalArgumentException("Board name must be at most 255 characters");
        }
        this.value = value.trim();
    }
}
