package org.kanban.taskservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Comment(UUID id, UUID authorId, String content, Instant createdAt) {
    private static final int MAX_LENGTH = 2_000;

    public Comment {
        Objects.requireNonNull(id, "Comment id must not be null");
        Objects.requireNonNull(authorId, "Comment author id must not be null");
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Comment content must not be blank");
        }
        content = content.trim();
        if (content.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Comment content must not exceed " + MAX_LENGTH + " characters");
        }
        createdAt = createdAt == null ? Instant.now() : createdAt;
    }

    public static Comment create(UUID authorId, String content) {
        return new Comment(UUID.randomUUID(), authorId, content, Instant.now());
    }
}
