package org.kanban.taskservice.domain.exception;

import org.kanban.taskservice.domain.vo.TaskId;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(TaskId taskId) {
        super("Task not found: " + taskId.value());
    }
}
