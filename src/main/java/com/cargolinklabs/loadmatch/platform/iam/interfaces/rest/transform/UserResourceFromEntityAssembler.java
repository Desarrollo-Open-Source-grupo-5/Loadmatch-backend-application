package com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.resources.UserResource;

/**
 * Assembler that translates a user into its response resource
 */
public final class UserResourceFromEntityAssembler {

    private UserResourceFromEntityAssembler() {
    }

    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(user.getId().userId(), user.getEmail().address(), user.getRoleName(), user.isActive());
    }
}
