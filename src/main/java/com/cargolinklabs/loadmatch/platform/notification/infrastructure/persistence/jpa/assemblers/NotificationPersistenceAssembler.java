package com.cargolinklabs.loadmatch.platform.notification.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.notification.domain.model.aggregates.Notification;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.valueobjects.NotificationId;
import com.cargolinklabs.loadmatch.platform.notification.infrastructure.persistence.jpa.entities.NotificationPersistenceEntity;

public final class NotificationPersistenceAssembler {
    private NotificationPersistenceAssembler() {
    }

    public static NotificationPersistenceEntity toPersistenceFromDomain(Notification notification) {
        var entity = new NotificationPersistenceEntity();
        entity.setId(notification.getId().value());
        entity.setUserId(notification.getUserId());
        entity.setType(notification.getType());
        entity.setTitle(notification.getTitle());
        entity.setMessage(notification.getMessage());
        entity.setUrgent(notification.isUrgent());
        entity.setRead(!notification.isPending());
        entity.setCreatedAt(notification.getCreatedAt());
        entity.setReadAt(notification.getReadAt());
        return entity;
    }

    public static Notification toDomainFromPersistence(NotificationPersistenceEntity entity) {
        return Notification.reconstitute(new NotificationId(entity.getId()), entity.getUserId(), entity.getType(),
                entity.getTitle(), entity.getMessage(), entity.isUrgent(), entity.getCreatedAt(), entity.getReadAt());
    }
}
