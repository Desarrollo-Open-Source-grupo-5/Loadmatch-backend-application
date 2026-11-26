package com.cargolinklabs.loadmatch.platform.notification.domain.model.queries;

import java.util.UUID;

public record GetNotificationsByUserIdQuery(UUID userId, String requesterEmail) {
}
