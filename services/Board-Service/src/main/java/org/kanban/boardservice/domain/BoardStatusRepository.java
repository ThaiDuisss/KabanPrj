package org.kanban.boardservice.domain;

import org.kanban.boardservice.domain.entity.BoardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardStatusRepository extends JpaRepository<BoardStatus, Long> {
    List<BoardStatus> findByBoard_IdOrderByPositionAsc(Long boardId);
}
