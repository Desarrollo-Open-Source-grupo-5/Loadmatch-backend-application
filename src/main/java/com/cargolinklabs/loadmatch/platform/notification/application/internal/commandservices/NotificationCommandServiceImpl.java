package com.cargolinklabs.loadmatch.platform.notification.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.notification.application.commandservices.NotificationCommandService;
import com.cargolinklabs.loadmatch.platform.notification.application.internal.outboundservices.acl.NotificationOwnerAccess;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.aggregates.Notification;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.commands.CreateNotificationCommand;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.commands.MarkNotificationsAsReadCommand;
import com.cargolinklabs.loadmatch.platform.notification.domain.repositories.NotificationRepository;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class NotificationCommandServiceImpl implements NotificationCommandService {
    private final NotificationRepository notificationRepository;
    private final NotificationOwnerAccess ownerAccess;

    public NotificationCommandServiceImpl(NotificationRepository notificationRepository, NotificationOwnerAccess ownerAccess) {
        this.notificationRepository = notificationRepository;
        this.ownerAccess = ownerAccess;
    }

    @Override
    @Transactional
    public void handle(CreateNotificationCommand command) {
        notificationRepository.save(new Notification(command.userId(), command.type(), command.title(),
                command.message(), command.urgent()));
    }

    @Override
    @Transactional
    public Result<Integer, ApplicationError> handle(MarkNotificationsAsReadCommand command) {
        return ownerAccess.forOwner(command.userId(), command.requesterEmail(), () -> {
            var notifications = notificationRepository.findUnreadByUserIdForUpdate(command.userId());
            var readAt = Instant.now();
            for (var notification : notifications) {
                notification.markAsRead(readAt);
                notificationRepository.save(notification);
            }
            return notifications.size();
        });
    }
}
