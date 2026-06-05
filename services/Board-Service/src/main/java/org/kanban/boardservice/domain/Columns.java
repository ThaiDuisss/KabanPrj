package org.kanban.boardservice.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "columns")
public class Columns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
}
