package org.kanban.boardservice.api.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kanban.boardservice.domain.BoardMembers;
import org.kanban.boardservice.domain.Columns;

import java.sql.Timestamp;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardsResponse {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<Columns> columns;
    private List<BoardMembers> boardMembers;
}
