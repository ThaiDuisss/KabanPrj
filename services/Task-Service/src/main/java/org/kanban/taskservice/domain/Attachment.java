package org.kanban.taskservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Attachment(UUID id, String fileName, String url, long sizeInBytes, Instant uploadedAt) {
    public Attachment {
        Objects.requireNonNull(id, "Attachment id must not be null");
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Attachment file name must not be blank");
        }
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Attachment url must not be blank");
        }
        if (sizeInBytes < 0) {
            throw new IllegalArgumentException("Attachment size must not be negative");
        }
        fileName = fileName.trim();
        url = url.trim();
        uploadedAt = uploadedAt == null ? Instant.now() : uploadedAt;
    }

    public static Attachment upload(String fileName, String url, long sizeInBytes) {
        return new Attachment(UUID.randomUUID(), fileName, url, sizeInBytes, Instant.now());
    }
}
