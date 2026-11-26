package com.cargolinklabs.loadmatch.platform.notification.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.notification.domain.model.valueobjects.NotificationId;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.valueobjects.NotificationType;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Notification {
    private final NotificationId id;
    private final UUID userId;
    private final NotificationType type;
    private final String title;
    private final String message;
    private final boolean urgent;
    private final Instant createdAt;
    private Instant readAt;

    public Notification(UUID userId, NotificationType type, String title, String message, boolean urgent) {
        this(new NotificationId(), userId, type, title, message, urgent, Instant.now(), null);
    }

    private Notification(NotificationId id, UUID userId, NotificationType type, String title, String message,
                         boolean urgent, Instant createdAt, Instant readAt) {
        this.id = Objects.requireNonNull(id);
        this.userId = Objects.requireNonNull(userId);
        this.type = Objects.requireNonNull(type);
        if (title == null || title.isBlank() || title.length() > 120) {
            throw new IllegalArgumentException("Notification title must contain between 1 and 120 characters");
        }
        if (message == null || message.isBlank() || message.length() > 500) {
            throw new IllegalArgumentException("Notification message must contain between 1 and 500 characters");
        }
        this.title = title;
        this.message = message;
        this.urgent = urgent;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.readAt = readAt;
    }

    public static Notification reconstitute(NotificationId id, UUID userId, NotificationType type, String title,
                                             String message, boolean urgent, Instant createdAt, Instant readAt) {
        return new Notification(id, userId, type, title, message, urgent, createdAt, readAt);
    }

    public boolean isPending() {
        return readAt == null;
    }

    public void markAsRead(Instant readAt) {
        if (isPending()) {
            this.readAt = Objects.requireNonNull(readAt);
        }
    }
}
