package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.RegisterCarrierCommand;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.RegisterCarrierResource;

/**
 * Assembler that translates a carrier registration resource into a command
 */
public final class RegisterCarrierCommandFromResourceAssembler {

    private RegisterCarrierCommandFromResourceAssembler() {
    }

    public static RegisterCarrierCommand toCommandFromResource(RegisterCarrierResource resource) {
        return new RegisterCarrierCommand(
                resource.email(),
                resource.password(),
                resource.firstNames(),
                resource.lastNames(),
                resource.dni(),
                resource.licenseNumber(),
                resource.licenseCategory(),
                resource.licenseExpirationDate(),
                resource.phoneNumber(),
                resource.operatingZone());
    }
}
