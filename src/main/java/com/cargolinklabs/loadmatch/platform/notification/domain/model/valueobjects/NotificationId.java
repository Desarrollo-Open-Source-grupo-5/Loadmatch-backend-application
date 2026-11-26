package com.cargolinklabs.loadmatch.platform.notification.domain.model.valueobjects;

import java.util.UUID;

public record NotificationId(UUID value) {
    public NotificationId {
        if (value == null) {
            throw new IllegalArgumentException("Notification id is required");
        }
    }

    public NotificationId() {
        this(UUID.randomUUID());
    }
}
