package com.cargolinklabs.loadmatch.platform.notification.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.iam.interfaces.acl.IamContextFacade;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Supplier;

@Component
public class NotificationOwnerAccess {
    private final IamContextFacade iamContextFacade;

    public NotificationOwnerAccess(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public <T> Result<T, ApplicationError> forOwner(UUID userId, String requesterEmail, Supplier<T> operation) {
        if (iamContextFacade.fetchUserIdByEmail(requesterEmail).filter(userId::equals).isEmpty()) {
            return Result.failure(ApplicationError.forbidden("notification-owner", ""));
        }
        return Result.success(operation.get());
    }
}
