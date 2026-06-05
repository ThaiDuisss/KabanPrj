package org.kanban.taskservice.domain.exception;

public class InvalidAssigneeException extends RuntimeException {
    public InvalidAssigneeException(String message) {
        super(message);
    }
}
