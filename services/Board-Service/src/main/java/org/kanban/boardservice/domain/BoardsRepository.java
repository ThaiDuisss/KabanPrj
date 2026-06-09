package org.kanban.boardservice.domain;

import org.kanban.boardservice.domain.aggregate.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardsRepository extends JpaRepository<Board, Long> {
}
