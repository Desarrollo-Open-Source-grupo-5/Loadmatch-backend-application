package com.cargolinklabs.loadmatch.platform.freight.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.commands.CreateLoadRequestCommand;
import com.cargolinklabs.loadmatch.platform.freight.interfaces.rest.resources.CreateLoadRequestResource;

/**
 * Assembler that translates a load request creation resource into a command
 */
public final class CreateLoadRequestCommandFromResourceAssembler {

    private CreateLoadRequestCommandFromResourceAssembler() {
    }

    public static CreateLoadRequestCommand toCommandFromResource(CreateLoadRequestResource resource) {
        return new CreateLoadRequestCommand(
                resource.shipperId(),
                resource.vehicleTypeId(),
                resource.originAddress(),
                resource.originDistrict(),
                resource.originLatitude(),
                resource.originLongitude(),
                resource.destinationAddress(),
                resource.destinationDistrict(),
                resource.destinationLatitude(),
                resource.destinationLongitude(),
                resource.weightKg(),
                resource.lengthM(),
                resource.widthM(),
                resource.heightM(),
                resource.cargoType(),
                resource.offeredRateAmount(),
                resource.pickupAt());
    }
}
