package com.cargolinklabs.loadmatch.platform.notification.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.notification.domain.model.valueobjects.NotificationType;

import java.util.UUID;

public record CreateNotificationCommand(UUID userId, NotificationType type, String title, String message,
                                        boolean urgent) {
}
