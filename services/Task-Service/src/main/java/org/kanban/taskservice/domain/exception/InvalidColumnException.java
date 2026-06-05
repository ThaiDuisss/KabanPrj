package org.kanban.taskservice.domain.exception;

public class InvalidColumnException extends RuntimeException {
    public InvalidColumnException(String message) {
        super(message);
    }
}
