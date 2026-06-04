package org.kanban.boardservice.domain;
import jakarta.persistence.*;
import lombok.*;

import java.sql.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board_members")
public class BoardMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Column(name = "joined_at", nullable = false)
    private Timestamp joinedAt;

}
