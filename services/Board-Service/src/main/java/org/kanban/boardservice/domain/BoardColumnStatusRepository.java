package org.kanban.boardservice.domain;

import org.kanban.boardservice.domain.entity.BoardColumnStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardColumnStatusRepository extends JpaRepository<BoardColumnStatus, Long> {
    List<BoardColumnStatus> findByColumn_IdOrderByPositionAsc(Long columnId);
}
