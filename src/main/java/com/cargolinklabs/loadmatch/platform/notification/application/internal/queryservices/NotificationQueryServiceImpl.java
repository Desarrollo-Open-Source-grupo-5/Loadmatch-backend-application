package com.cargolinklabs.loadmatch.platform.notification.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.notification.application.internal.outboundservices.acl.NotificationOwnerAccess;
import com.cargolinklabs.loadmatch.platform.notification.application.queryservices.NotificationQueryService;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.aggregates.Notification;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.queries.CountUnreadNotificationsQuery;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.queries.GetNotificationsByUserIdQuery;
import com.cargolinklabs.loadmatch.platform.notification.domain.repositories.NotificationRepository;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class NotificationQueryServiceImpl implements NotificationQueryService {
    private final NotificationRepository notificationRepository;
    private final NotificationOwnerAccess ownerAccess;

    public NotificationQueryServiceImpl(NotificationRepository notificationRepository, NotificationOwnerAccess ownerAccess) {
        this.notificationRepository = notificationRepository;
        this.ownerAccess = ownerAccess;
    }

    @Override
    public Result<List<Notification>, ApplicationError> handle(GetNotificationsByUserIdQuery query) {
        return ownerAccess.forOwner(query.userId(), query.requesterEmail(),
                () -> notificationRepository.findByUserId(query.userId()));
    }

    @Override
    public Result<Long, ApplicationError> handle(CountUnreadNotificationsQuery query) {
        return ownerAccess.forOwner(query.userId(), query.requesterEmail(),
                () -> notificationRepository.countUnread(query.userId()));
    }
}
