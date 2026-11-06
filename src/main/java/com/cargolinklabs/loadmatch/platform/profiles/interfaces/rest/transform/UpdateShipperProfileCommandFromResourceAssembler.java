package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.UpdateShipperProfileCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.UpdateShipperProfileResource;

import java.util.UUID;

/**
 * Assembler that translates a shipper profile update resource into a command
 */
public final class UpdateShipperProfileCommandFromResourceAssembler {

    private UpdateShipperProfileCommandFromResourceAssembler() {
    }

    public static UpdateShipperProfileCommand toCommandFromResource(UUID shipperId, UpdateShipperProfileResource resource) {
        return new UpdateShipperProfileCommand(
                new ShipperId(shipperId),
                resource.contactName(),
                resource.phoneNumber(),
                resource.street(),
                resource.district(),
                resource.province(),
                resource.department());
    }
}
