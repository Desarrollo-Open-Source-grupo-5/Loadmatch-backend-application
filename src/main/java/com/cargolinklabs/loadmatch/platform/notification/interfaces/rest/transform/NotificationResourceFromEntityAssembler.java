package com.cargolinklabs.loadmatch.platform.notification.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.notification.domain.model.aggregates.Notification;
import com.cargolinklabs.loadmatch.platform.notification.interfaces.rest.resources.NotificationResource;

public final class NotificationResourceFromEntityAssembler {
    private NotificationResourceFromEntityAssembler() {
    }

    public static NotificationResource toResourceFromEntity(Notification notification) {
        return new NotificationResource(notification.getId().value(), notification.getUserId(),
                notification.getType().name(), notification.getTitle(), notification.getMessage(),
                notification.isUrgent(), !notification.isPending(), notification.getCreatedAt(), notification.getReadAt());
    }
}
