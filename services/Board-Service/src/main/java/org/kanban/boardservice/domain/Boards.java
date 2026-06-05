package org.kanban.boardservice.domain;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.sql.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boards")
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    private List<Columns> columns;
    private List<BoardMembers> boardMembers;
}
