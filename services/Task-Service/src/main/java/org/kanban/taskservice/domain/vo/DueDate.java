package org.kanban.taskservice.domain.vo;

import java.time.LocalDate;

public record DueDate(LocalDate value) {
    public boolean isOverdue(LocalDate today) {
        return value != null && value.isBefore(today);
    }
}
