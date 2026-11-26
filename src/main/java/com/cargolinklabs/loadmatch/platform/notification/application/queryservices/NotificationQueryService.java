package com.cargolinklabs.loadmatch.platform.notification.application.queryservices;

import com.cargolinklabs.loadmatch.platform.notification.domain.model.aggregates.Notification;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.queries.CountUnreadNotificationsQuery;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.queries.GetNotificationsByUserIdQuery;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

import java.util.List;

public interface NotificationQueryService {
    Result<List<Notification>, ApplicationError> handle(GetNotificationsByUserIdQuery query);

    Result<Long, ApplicationError> handle(CountUnreadNotificationsQuery query);
}
