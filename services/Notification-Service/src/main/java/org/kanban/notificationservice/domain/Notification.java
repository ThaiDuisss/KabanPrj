package org.kanban.notificationservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue
    private String id;
    private String userId;
    private String message;
    private boolean read;

    public static Notification create(String userId, String message) {
        return Notification.builder()
                .userId(userId)
                .message(message)
                .read(false)
                .build();
    }
}
