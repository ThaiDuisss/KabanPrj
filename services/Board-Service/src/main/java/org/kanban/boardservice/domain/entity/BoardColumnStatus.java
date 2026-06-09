package org.kanban.boardservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "board_column_statuses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardColumnStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_column_id", nullable = false)
    private BoardColumn column;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_status_id", nullable = false)
    private BoardStatus status;

    @Column(name = "position", nullable = false)
    private Integer position;

    public BoardColumnStatus(BoardColumn column, BoardStatus status, int position) {
        if (column == null) {
            throw new IllegalArgumentException("Column is required");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status is required");
        }
        if (position < 0) {
            throw new IllegalArgumentException("Column status position must be greater than or equal to 0");
        }
        this.column = column;
        this.status = status;
        this.position = position;
    }
}
