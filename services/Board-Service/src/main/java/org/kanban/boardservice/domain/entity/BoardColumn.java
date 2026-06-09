package org.kanban.boardservice.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kanban.boardservice.domain.aggregate.Board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Entity
@Table(name = "board_columns")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "wip_limit")
    private Integer wipLimit;

    @Column(name = "is_done_column", nullable = false)
    private boolean doneColumn;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardColumnStatus> statusMappings = new ArrayList<>();

    public BoardColumn(Board board, String name, int position, Integer wipLimit, boolean doneColumn) {
        this.board = requireBoard(board);
        rename(name);
        moveTo(position);
        changeWipLimit(wipLimit);
        this.doneColumn = doneColumn;
    }

    public void rename(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Column name must not be blank");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Column name must be at most 100 characters");
        }
        this.name = name.trim();
    }

    public void moveTo(int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Column position must be greater than or equal to 0");
        }
        this.position = position;
    }

    public void changeWipLimit(Integer wipLimit) {
        if (wipLimit != null && wipLimit <= 0) {
            throw new IllegalArgumentException("WIP limit must be greater than 0");
        }
        this.wipLimit = wipLimit;
    }

    public BoardColumnStatus mapStatus(BoardStatus status, int position) {
        BoardColumnStatus mapping = new BoardColumnStatus(this, status, position);
        statusMappings.add(mapping);
        return mapping;
    }

    public List<BoardColumnStatus> getStatusMappings() {
        return Collections.unmodifiableList(statusMappings);
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private Board requireBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board is required");
        }
        return board;
    }
}
