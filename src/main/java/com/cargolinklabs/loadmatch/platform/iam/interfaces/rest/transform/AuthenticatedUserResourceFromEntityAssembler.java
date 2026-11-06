package com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

/**
 * Assembler that translates an authenticated user into its response resource
 */
public final class AuthenticatedUserResourceFromEntityAssembler {

    private AuthenticatedUserResourceFromEntityAssembler() {
    }

    public static AuthenticatedUserResource toResourceFromEntity(User user) {
        return new AuthenticatedUserResource(user.getId().userId(), user.getEmail().address(), user.getRoleName());
    }
}
