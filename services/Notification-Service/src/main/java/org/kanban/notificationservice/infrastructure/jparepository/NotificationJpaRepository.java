package org.kanban.notificationservice.infrastructure.jparepository;

import org.kanban.notificationservice.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, String> {
}
