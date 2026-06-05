package org.kanban.taskservice.domain.vo;

public record TaskTitle(String value) {
    private static final int MAX_LENGTH = 200;

    public TaskTitle {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank");
        }
        value = value.trim();
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Task title must not exceed " + MAX_LENGTH + " characters");
        }
    }
}
