package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.UpdateCarrierProfileCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.UpdateCarrierProfileResource;

import java.util.UUID;

/**
 * Assembler that translates a carrier profile update resource into a command
 */
public final class UpdateCarrierProfileCommandFromResourceAssembler {

    private UpdateCarrierProfileCommandFromResourceAssembler() {
    }

    public static UpdateCarrierProfileCommand toCommandFromResource(UUID carrierId, UpdateCarrierProfileResource resource) {
        return new UpdateCarrierProfileCommand(
                new CarrierId(carrierId),
                resource.phoneNumber(),
                resource.operatingZone(),
                resource.photoUrl());
    }
}
