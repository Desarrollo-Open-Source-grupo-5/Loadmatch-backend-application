package com.cargolinklabs.loadmatch.platform.notification.domain.model.commands;

import java.util.UUID;

public record MarkNotificationsAsReadCommand(UUID userId, String requesterEmail) {
}
