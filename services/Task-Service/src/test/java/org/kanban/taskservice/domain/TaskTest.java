package org.kanban.taskservice.domain;

import org.junit.jupiter.api.Test;
import org.kanban.taskservice.domain.exception.InvalidAssigneeException;
import org.kanban.taskservice.domain.vo.BoardId;
import org.kanban.taskservice.domain.vo.ColumnId;
import org.kanban.taskservice.domain.vo.TaskPosition;
import org.kanban.taskservice.domain.vo.TaskPriority;
import org.kanban.taskservice.domain.vo.TaskStatus;
import org.kanban.taskservice.domain.vo.TaskTitle;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskTest {
    @Test
    void createTaskWithDefaults() {
        BoardId boardId = new BoardId(UUID.randomUUID());
        ColumnId columnId = new ColumnId(UUID.randomUUID());

        Task task = Task.create(boardId, columnId, new TaskTitle("Implement aggregate"), TaskPosition.first());

        assertThat(task.getId()).isNotNull();
        assertThat(task.getBoardId()).isEqualTo(boardId);
        assertThat(task.getColumnId()).isEqualTo(columnId);
        assertThat(task.getTitle().value()).isEqualTo("Implement aggregate");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(task.getAssignees()).isEmpty();
        assertThat(task.getComments()).isEmpty();
        assertThat(task.getAttachments()).isEmpty();
    }

    @Test
    void updateTaskContentAndWorkflow() {
        Task task = Task.create(
                new BoardId(UUID.randomUUID()),
                new ColumnId(UUID.randomUUID()),
                new TaskTitle("Initial title"),
                TaskPosition.first()
        );

        task.rename(new TaskTitle("Updated title"));
        task.updateDescription(" Add aggregate behavior ");
        task.changePriority(TaskPriority.HIGH);
        task.startProgress();
        task.complete();

        assertThat(task.getTitle().value()).isEqualTo("Updated title");
        assertThat(task.getDescription()).contains("Add aggregate behavior");
        assertThat(task.getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.DONE);
    }

    @Test
    void rejectDuplicateAssignee() {
        Task task = Task.create(
                new BoardId(UUID.randomUUID()),
                new ColumnId(UUID.randomUUID()),
                new TaskTitle("Assign user"),
                TaskPosition.first()
        );
        UUID userId = UUID.randomUUID();

        task.assignTo(userId);

        assertThatThrownBy(() -> task.assignTo(userId))
                .isInstanceOf(InvalidAssigneeException.class);
    }

    @Test
    void archivedTaskCannotBeChanged() {
        Task task = Task.create(
                new BoardId(UUID.randomUUID()),
                new ColumnId(UUID.randomUUID()),
                new TaskTitle("Archive task"),
                TaskPosition.first()
        );

        task.archive();

        assertThat(task.isArchived()).isTrue();
        assertThat(task.getArchivedAt()).isPresent();
        assertThatThrownBy(() -> task.rename(new TaskTitle("Cannot change")))
                .isInstanceOf(IllegalStateException.class);
    }
}
