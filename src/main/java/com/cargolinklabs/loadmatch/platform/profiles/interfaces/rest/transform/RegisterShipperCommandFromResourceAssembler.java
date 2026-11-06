package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.RegisterShipperCommand;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.RegisterShipperResource;

/**
 * Assembler that translates a shipper registration resource into a command
 */
public final class RegisterShipperCommandFromResourceAssembler {

    private RegisterShipperCommandFromResourceAssembler() {
    }

    public static RegisterShipperCommand toCommandFromResource(RegisterShipperResource resource) {
        return new RegisterShipperCommand(
                resource.email(),
                resource.password(),
                resource.ruc(),
                resource.businessName(),
                resource.contactName(),
                resource.phoneNumber(),
                resource.street(),
                resource.district(),
                resource.province(),
                resource.department());
    }
}
