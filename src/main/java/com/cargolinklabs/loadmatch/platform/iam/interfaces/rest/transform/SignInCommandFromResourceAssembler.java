package com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.commands.SignInCommand;
import com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.resources.SignInResource;

/**
 * Assembler that translates a sign in resource into a sign in command
 */
public final class SignInCommandFromResourceAssembler {

    private SignInCommandFromResourceAssembler() {
    }

    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.email(), resource.password());
    }
}
