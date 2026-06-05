package org.kanban.taskservice.domain.vo;

import java.util.Objects;
import java.util.UUID;

public record TaskId(UUID value) {
    public TaskId {
        Objects.requireNonNull(value, "Task id must not be null");
    }

    public static TaskId newId() {
        return new TaskId(UUID.randomUUID());
    }

    public static TaskId from(String value) {
        return new TaskId(UUID.fromString(value));
    }
}
