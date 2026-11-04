package com.cargolinklabs.loadmatch.platform.iam.application.acl;

import com.cargolinklabs.loadmatch.platform.iam.application.commandservices.UserCommandService;
import com.cargolinklabs.loadmatch.platform.iam.application.queryservices.UserQueryService;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.commands.SignUpCommand;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.EmailAddress;
import com.cargolinklabs.loadmatch.platform.iam.interfaces.acl.IamContextFacade;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * IAM context facade implementation that delegates to the local command and query services
 */
@Service
public class IamContextFacadeImpl implements IamContextFacade {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IamContextFacadeImpl(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @Override
    public Result<UUID, ApplicationError> createUser(String email, String password, String role) {
        try {
            return userCommandService.handle(new SignUpCommand(email, password, role))
                    .map(user -> user.getId().userId());
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("user", e.getMessage()));
        }
    }

    @Override
    public Optional<UUID> fetchUserIdByEmail(String email) {
        return userQueryService.handle(new GetUserByEmailQuery(new EmailAddress(email)))
                .map(user -> user.getId().userId());
    }
}
