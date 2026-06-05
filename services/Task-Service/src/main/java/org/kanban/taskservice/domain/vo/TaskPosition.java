package org.kanban.taskservice.domain.vo;

public record TaskPosition(int value) implements Comparable<TaskPosition> {
    public TaskPosition {
        if (value < 0) {
            throw new IllegalArgumentException("Task position must not be negative");
        }
    }

    public static TaskPosition first() {
        return new TaskPosition(0);
    }

    @Override
    public int compareTo(TaskPosition other) {
        return Integer.compare(value, other.value);
    }
}
