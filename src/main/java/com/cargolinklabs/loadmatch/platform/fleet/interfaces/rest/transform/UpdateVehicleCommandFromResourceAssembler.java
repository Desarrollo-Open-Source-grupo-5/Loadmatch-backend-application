package com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.UpdateVehicleCommand;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleId;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources.UpdateVehicleResource;

import java.util.UUID;

/**
 * Assembler that translates a vehicle update resource into a command
 */
public final class UpdateVehicleCommandFromResourceAssembler {

    private UpdateVehicleCommandFromResourceAssembler() {
    }

    public static UpdateVehicleCommand toCommandFromResource(UUID vehicleId, UpdateVehicleResource resource) {
        return new UpdateVehicleCommand(
                new VehicleId(vehicleId),
                resource.vehicleTypeId(),
                resource.brand(),
                resource.model(),
                resource.manufactureYear(),
                resource.payloadKg());
    }
}
