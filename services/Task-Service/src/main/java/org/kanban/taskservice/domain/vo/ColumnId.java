package org.kanban.taskservice.domain.vo;

import java.util.Objects;
import java.util.UUID;

public record ColumnId(UUID value) {
    public ColumnId {
        Objects.requireNonNull(value, "Column id must not be null");
    }

    public static ColumnId from(String value) {
        return new ColumnId(UUID.fromString(value));
    }
}
