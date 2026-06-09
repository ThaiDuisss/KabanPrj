package org.kanban.boardservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kanban.boardservice.domain.aggregate.Board;
import org.kanban.boardservice.domain.valueobject.StatusCategory;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "board_statuses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_category", nullable = false, length = 30)
    private StatusCategory category;

    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public BoardStatus(Board board, String name, StatusCategory category, int position) {
        if (board == null) {
            throw new IllegalArgumentException("Board is required");
        }
        this.board = board;
        rename(name);
        changeCategory(category);
        moveTo(position);
    }

    public void rename(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Status name must not be blank");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Status name must be at most 100 characters");
        }
        this.name = name.trim();
    }

    public void changeCategory(StatusCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Status category is required");
        }
        this.category = category;
    }

    public void moveTo(int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Status position must be greater than or equal to 0");
        }
        this.position = position;
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
}
