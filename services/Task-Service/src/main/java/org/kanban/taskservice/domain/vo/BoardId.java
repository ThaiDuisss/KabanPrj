package org.kanban.taskservice.domain.vo;

import java.util.Objects;
import java.util.UUID;

public record BoardId(UUID value) {
    public BoardId {
        Objects.requireNonNull(value, "Board id must not be null");
    }

    public static BoardId from(String value) {
        return new BoardId(UUID.fromString(value));
    }
}
