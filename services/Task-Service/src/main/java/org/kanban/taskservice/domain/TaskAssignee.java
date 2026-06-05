package org.kanban.taskservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record TaskAssignee(UUID userId, Instant assignedAt) {
    public TaskAssignee {
        Objects.requireNonNull(userId, "Assignee user id must not be null");
        assignedAt = assignedAt == null ? Instant.now() : assignedAt;
    }

    public static TaskAssignee assign(UUID userId) {
        return new TaskAssignee(userId, Instant.now());
    }
}
