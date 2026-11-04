package com.cargolinklabs.loadmatch.platform.iam.interfaces.acl;

import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

import java.util.Optional;
import java.util.UUID;

/**
 * IAM context facade
 * <p>
 *     Entry point that other bounded contexts use to interact with IAM without depending on its internals.
 * </p>
 */
public interface IamContextFacade {
    /**
     * Creates a user account
     * @param email the email address
     * @param password the raw password
     * @param role the role name (SHIPPER or CARRIER)
     * @return the identifier of the new user or an application error
     */
    Result<UUID, ApplicationError> createUser(String email, String password, String role);

    /**
     * Fetches the identifier of a user by its email address
     * @param email the email address
     * @return the user identifier when the user exists
     */
    Optional<UUID> fetchUserIdByEmail(String email);
}
