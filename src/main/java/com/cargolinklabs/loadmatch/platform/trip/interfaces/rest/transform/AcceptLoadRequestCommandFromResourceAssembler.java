package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.AcceptLoadRequestCommand;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.AcceptLoadRequestResource;

/**
 * Assembler that translates a load request acceptance resource into a command
 */
public final class AcceptLoadRequestCommandFromResourceAssembler {

    private AcceptLoadRequestCommandFromResourceAssembler() {
    }

    public static AcceptLoadRequestCommand toCommandFromResource(AcceptLoadRequestResource resource) {
        return new AcceptLoadRequestCommand(resource.loadRequestId(), resource.carrierId(), resource.vehicleId());
    }
}
