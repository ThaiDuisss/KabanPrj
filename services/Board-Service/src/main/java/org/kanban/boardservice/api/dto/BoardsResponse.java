package org.kanban.boardservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kanban.boardservice.domain.entity.BoardColumn;
import org.kanban.boardservice.domain.entity.BoardMember;
import org.kanban.boardservice.domain.entity.BoardStatus;
import org.kanban.boardservice.domain.valueobject.BoardType;
import org.kanban.boardservice.domain.valueobject.BoardVisibility;

import java.time.LocalDateTime;
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
    private BoardVisibility visibility;
    private BoardType boardType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BoardColumn> columns;
    private List<BoardStatus> statuses;
    private List<BoardMember> boardMembers;
}
