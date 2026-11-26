package com.cargolinklabs.loadmatch.platform.notification.domain.repositories;

import com.cargolinklabs.loadmatch.platform.notification.domain.model.aggregates.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository {
    List<Notification> findByUserId(UUID userId);

    List<Notification> findUnreadByUserIdForUpdate(UUID userId);

    long countUnread(UUID userId);

    void save(Notification notification);
}
