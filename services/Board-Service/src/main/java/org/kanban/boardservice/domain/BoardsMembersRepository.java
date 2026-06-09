package org.kanban.boardservice.domain;

import org.kanban.boardservice.domain.entity.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardsMembersRepository extends JpaRepository<BoardMember, Long> {
    List<BoardMember> findByBoard_Id(Long boardId);

    Optional<BoardMember> findByBoard_IdAndUserId(Long boardId, Long userId);
}
