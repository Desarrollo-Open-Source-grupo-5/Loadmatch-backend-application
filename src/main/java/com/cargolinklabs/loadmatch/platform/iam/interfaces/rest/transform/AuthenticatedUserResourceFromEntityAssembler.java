package com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.iam.application.commandservices.AuthenticatedUser;
import com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

/**
 * Assembler that translates an authenticated user into its response resource
 */
public final class AuthenticatedUserResourceFromEntityAssembler {

    private AuthenticatedUserResourceFromEntityAssembler() {
    }

    public static AuthenticatedUserResource toResourceFromEntity(AuthenticatedUser authenticatedUser) {
        var user = authenticatedUser.user();
        return new AuthenticatedUserResource(user.getId().userId(), user.getEmail().address(),
                user.getRoleName(), authenticatedUser.token());
    }
}
