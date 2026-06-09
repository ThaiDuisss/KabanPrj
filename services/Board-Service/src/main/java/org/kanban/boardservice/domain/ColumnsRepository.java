package org.kanban.boardservice.domain;

import org.kanban.boardservice.domain.entity.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColumnsRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findByBoard_IdOrderByPositionAsc(Long boardId);
}
