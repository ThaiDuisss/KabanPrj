package org.kanban.notificationservice.domain.repository;

import org.kanban.notificationservice.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification job);

    Optional<Notification> findById(String id);

    Page<Notification> search(String keyword, Pageable pageable);

    Page<Notification> searchPublished(String keyword, Pageable pageable);

    boolean existsByTitle(String title);
}
