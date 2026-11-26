package com.cargolinklabs.loadmatch.platform.notification.domain.model.queries;

import java.util.UUID;

public record CountUnreadNotificationsQuery(UUID userId, String requesterEmail) {
}
