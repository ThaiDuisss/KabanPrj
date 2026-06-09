package org.kanban.boardservice.domain.aggregate;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kanban.boardservice.domain.entity.BoardColumn;
import org.kanban.boardservice.domain.entity.BoardMember;
import org.kanban.boardservice.domain.entity.BoardStatus;
import org.kanban.boardservice.domain.valueobject.BoardDescription;
import org.kanban.boardservice.domain.valueobject.BoardName;
import org.kanban.boardservice.domain.valueobject.BoardRole;
import org.kanban.boardservice.domain.valueobject.BoardType;
import org.kanban.boardservice.domain.valueobject.BoardVisibility;
import org.kanban.boardservice.domain.valueobject.StatusCategory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Entity
@Table(name = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name", nullable = false, length = 255))
    private BoardName name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "description", length = 1000))
    private BoardDescription description;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false, length = 30)
    private BoardVisibility visibility = BoardVisibility.PRIVATE;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type", nullable = false, length = 30)
    private BoardType boardType = BoardType.KANBAN;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardColumn> columns = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardStatus> statuses = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardMember> members = new ArrayList<>();

    public Board(BoardName name, BoardDescription description, Long ownerId, BoardVisibility visibility, BoardType boardType) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Board owner is required");
        }
        this.name = name;
        this.description = description == null ? new BoardDescription(null) : description;
        this.ownerId = ownerId;
        this.visibility = visibility == null ? BoardVisibility.PRIVATE : visibility;
        this.boardType = boardType == null ? BoardType.KANBAN : boardType;
        addMember(ownerId, BoardRole.OWNER);
    }

    public void rename(BoardName name) {
        this.name = name;
    }

    public void changeDescription(BoardDescription description) {
        this.description = description == null ? new BoardDescription(null) : description;
    }

    public void changeVisibility(BoardVisibility visibility) {
        this.visibility = visibility == null ? BoardVisibility.PRIVATE : visibility;
    }

    public BoardColumn addColumn(String name, int position, Integer wipLimit, boolean doneColumn) {
        BoardColumn column = new BoardColumn(this, name, position, wipLimit, doneColumn);
        columns.add(column);
        return column;
    }

    public BoardStatus addStatus(String name, StatusCategory category, int position) {
        BoardStatus status = new BoardStatus(this, name, category, position);
        statuses.add(status);
        return status;
    }

    public BoardMember addMember(Long userId, BoardRole role) {
        BoardMember member = new BoardMember(this, userId, role);
        members.add(member);
        return member;
    }

    public List<BoardColumn> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    public List<BoardStatus> getStatuses() {
        return Collections.unmodifiableList(statuses);
    }

    public List<BoardMember> getMembers() {
        return Collections.unmodifiableList(members);
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
