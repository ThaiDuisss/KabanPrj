package org.kanban.taskservice.domain;

import org.kanban.taskservice.domain.exception.InvalidAssigneeException;
import org.kanban.taskservice.domain.exception.InvalidColumnException;
import org.kanban.taskservice.domain.vo.BoardId;
import org.kanban.taskservice.domain.vo.ColumnId;
import org.kanban.taskservice.domain.vo.DueDate;
import org.kanban.taskservice.domain.vo.TaskId;
import org.kanban.taskservice.domain.vo.TaskPosition;
import org.kanban.taskservice.domain.vo.TaskPriority;
import org.kanban.taskservice.domain.vo.TaskStatus;
import org.kanban.taskservice.domain.vo.TaskTitle;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Task {
    private final TaskId id;
    private final BoardId boardId;
    private ColumnId columnId;
    private TaskTitle title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private TaskPosition position;
    private DueDate dueDate;
    private final List<TaskAssignee> assignees;
    private final List<Comment> comments;
    private final List<Attachment> attachments;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant archivedAt;

    private Task(
            TaskId id,
            BoardId boardId,
            ColumnId columnId,
            TaskTitle title,
            String description,
            TaskStatus status,
            TaskPriority priority,
            TaskPosition position,
            DueDate dueDate,
            List<TaskAssignee> assignees,
            List<Comment> comments,
            List<Attachment> attachments,
            Instant createdAt,
            Instant updatedAt,
            Instant archivedAt
    ) {
        this.id = Objects.requireNonNull(id, "Task id must not be null");
        this.boardId = Objects.requireNonNull(boardId, "Board id must not be null");
        this.columnId = Objects.requireNonNull(columnId, "Column id must not be null");
        this.title = Objects.requireNonNull(title, "Task title must not be null");
        this.description = normalizeDescription(description);
        this.status = Objects.requireNonNull(status, "Task status must not be null");
        this.priority = Objects.requireNonNull(priority, "Task priority must not be null");
        this.position = Objects.requireNonNull(position, "Task position must not be null");
        this.dueDate = dueDate;
        this.assignees = new ArrayList<>(Objects.requireNonNull(assignees, "Assignees must not be null"));
        this.comments = new ArrayList<>(Objects.requireNonNull(comments, "Comments must not be null"));
        this.attachments = new ArrayList<>(Objects.requireNonNull(attachments, "Attachments must not be null"));
        this.createdAt = Objects.requireNonNull(createdAt, "Created time must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated time must not be null");
        this.archivedAt = archivedAt;
    }

    public static Task create(BoardId boardId, ColumnId columnId, TaskTitle title, TaskPosition position) {
        Instant now = Instant.now();
        return new Task(
                TaskId.newId(),
                boardId,
                columnId,
                title,
                null,
                TaskStatus.TODO,
                TaskPriority.MEDIUM,
                position,
                null,
                List.of(),
                List.of(),
                List.of(),
                now,
                now,
                null
        );
    }

    public static Task restore(
            TaskId id,
            BoardId boardId,
            ColumnId columnId,
            TaskTitle title,
            String description,
            TaskStatus status,
            TaskPriority priority,
            TaskPosition position,
            DueDate dueDate,
            List<TaskAssignee> assignees,
            List<Comment> comments,
            List<Attachment> attachments,
            Instant createdAt,
            Instant updatedAt,
            Instant archivedAt
    ) {
        return new Task(
                id,
                boardId,
                columnId,
                title,
                description,
                status,
                priority,
                position,
                dueDate,
                assignees,
                comments,
                attachments,
                createdAt,
                updatedAt,
                archivedAt
        );
    }

    public void rename(TaskTitle title) {
        ensureActive();
        this.title = Objects.requireNonNull(title, "Task title must not be null");
        markUpdated();
    }

    public void updateDescription(String description) {
        ensureActive();
        this.description = normalizeDescription(description);
        markUpdated();
    }

    public void changePriority(TaskPriority priority) {
        ensureActive();
        this.priority = Objects.requireNonNull(priority, "Task priority must not be null");
        markUpdated();
    }

    public void schedule(DueDate dueDate) {
        ensureActive();
        this.dueDate = dueDate;
        markUpdated();
    }

    public void moveTo(ColumnId columnId, TaskPosition position) {
        ensureActive();
        if (Objects.equals(this.columnId, columnId) && Objects.equals(this.position, position)) {
            return;
        }
        this.columnId = Objects.requireNonNull(columnId, "Column id must not be null");
        this.position = Objects.requireNonNull(position, "Task position must not be null");
        this.status = statusFromColumnMove(this.status);
        markUpdated();
    }

    public void reorder(TaskPosition position) {
        ensureActive();
        this.position = Objects.requireNonNull(position, "Task position must not be null");
        markUpdated();
    }

    public void startProgress() {
        ensureActive();
        if (status == TaskStatus.DONE) {
            throw new InvalidColumnException("Done task cannot be moved back to in progress directly");
        }
        this.status = TaskStatus.IN_PROGRESS;
        markUpdated();
    }

    public void complete() {
        ensureActive();
        this.status = TaskStatus.DONE;
        markUpdated();
    }

    public void reopen() {
        ensureActive();
        this.status = TaskStatus.TODO;
        markUpdated();
    }

    public void archive() {
        if (status == TaskStatus.ARCHIVED) {
            return;
        }
        this.status = TaskStatus.ARCHIVED;
        this.archivedAt = Instant.now();
        markUpdated();
    }

    public void assignTo(UUID userId) {
        ensureActive();
        TaskAssignee assignee = TaskAssignee.assign(userId);
        boolean alreadyAssigned = assignees.stream()
                .anyMatch(existing -> existing.userId().equals(assignee.userId()));
        if (alreadyAssigned) {
            throw new InvalidAssigneeException("User is already assigned to this task");
        }
        assignees.add(assignee);
        markUpdated();
    }

    public void unassign(UUID userId) {
        ensureActive();
        boolean removed = assignees.removeIf(assignee -> assignee.userId().equals(userId));
        if (!removed) {
            throw new InvalidAssigneeException("User is not assigned to this task");
        }
        markUpdated();
    }

    public void addComment(UUID authorId, String content) {
        ensureActive();
        comments.add(Comment.create(authorId, content));
        markUpdated();
    }

    public void addAttachment(String fileName, String url, long sizeInBytes) {
        ensureActive();
        attachments.add(Attachment.upload(fileName, url, sizeInBytes));
        markUpdated();
    }

    public void removeAttachment(UUID attachmentId) {
        ensureActive();
        boolean removed = attachments.removeIf(attachment -> attachment.id().equals(attachmentId));
        if (removed) {
            markUpdated();
        }
    }

    public TaskId getId() {
        return id;
    }

    public BoardId getBoardId() {
        return boardId;
    }

    public ColumnId getColumnId() {
        return columnId;
    }

    public TaskTitle getTitle() {
        return title;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskPosition getPosition() {
        return position;
    }

    public Optional<DueDate> getDueDate() {
        return Optional.ofNullable(dueDate);
    }

    public List<TaskAssignee> getAssignees() {
        return Collections.unmodifiableList(assignees);
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public List<Attachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Optional<Instant> getArchivedAt() {
        return Optional.ofNullable(archivedAt);
    }

    public boolean isArchived() {
        return status == TaskStatus.ARCHIVED;
    }

    private void ensureActive() {
        if (isArchived()) {
            throw new IllegalStateException("Archived task cannot be changed");
        }
    }

    private void markUpdated() {
        this.updatedAt = Instant.now();
    }

    private static String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }
        return description.trim();
    }

    private static TaskStatus statusFromColumnMove(TaskStatus currentStatus) {
        return currentStatus == TaskStatus.ARCHIVED ? TaskStatus.ARCHIVED : currentStatus;
    }
}
