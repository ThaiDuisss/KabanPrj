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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kanban.boardservice.domain.aggregate.Board;
import org.kanban.boardservice.domain.valueobject.BoardRole;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "board_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private BoardRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    public BoardMember(Board board, Long userId, BoardRole role) {
        if (board == null) {
            throw new IllegalArgumentException("Board is required");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User is required");
        }
        this.board = board;
        this.userId = userId;
        changeRole(role);
    }

    public void changeRole(BoardRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Board member role is required");
        }
        this.role = role;
    }

    @PrePersist
    void onCreate() {
        joinedAt = LocalDateTime.now();
    }
}
