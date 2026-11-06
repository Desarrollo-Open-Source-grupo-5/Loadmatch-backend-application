package com.cargolinklabs.loadmatch.platform.iam.application.commandservices;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.commands.SignInCommand;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.commands.SignUpCommand;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

/**
 * User command service
 */
public interface UserCommandService {
    /**
     * Registers a new user account
     * @param command the sign up command
     * @return the created user or an application error
     */
    Result<User, ApplicationError> handle(SignUpCommand command);

    /**
     * Authenticates a user with its email and password
     * @param command the sign in command
     * @return the authenticated user or an application error
     */
    Result<User, ApplicationError> handle(SignInCommand command);
}
