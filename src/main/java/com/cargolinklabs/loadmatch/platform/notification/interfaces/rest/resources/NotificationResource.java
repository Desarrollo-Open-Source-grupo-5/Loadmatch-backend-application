package com.cargolinklabs.loadmatch.platform.notification.interfaces.rest.resources;

import java.time.Instant;
import java.util.UUID;

public record NotificationResource(UUID id, UUID userId, String type, String title, String message, boolean urgent,
                                   boolean read, Instant createdAt, Instant readAt) {
}
