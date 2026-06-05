package org.kanban.notificationservice.infrastructure.adapter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kanban.notificationservice.domain.Notification;
import org.kanban.notificationservice.domain.repository.NotificationRepository;
import org.kanban.notificationservice.infrastructure.jparepository.NotificationJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationAdapter implements NotificationRepository {
    NotificationJpaRepository notificationJpaRepository;

    @Override
    public Notification save(Notification job) {
        return null;
    }

    @Override
    public Optional<Notification> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Page<Notification> search(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Notification> searchPublished(String keyword, Pageable pageable) {
        return null;
    }

    @Override
    public boolean existsByTitle(String title) {
        return false;
    }
}
