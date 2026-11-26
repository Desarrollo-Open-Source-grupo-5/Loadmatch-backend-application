package com.cargolinklabs.loadmatch.platform.notification.application.commandservices;

import com.cargolinklabs.loadmatch.platform.notification.domain.model.commands.CreateNotificationCommand;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.commands.MarkNotificationsAsReadCommand;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

public interface NotificationCommandService {
    void handle(CreateNotificationCommand command);

    Result<Integer, ApplicationError> handle(MarkNotificationsAsReadCommand command);
}
