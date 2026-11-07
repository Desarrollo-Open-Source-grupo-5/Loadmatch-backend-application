package com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.RegisterVehicleCommand;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources.RegisterVehicleResource;

/**
 * Assembler that translates a vehicle registration resource into a command
 */
public final class RegisterVehicleCommandFromResourceAssembler {

    private RegisterVehicleCommandFromResourceAssembler() {
    }

    public static RegisterVehicleCommand toCommandFromResource(RegisterVehicleResource resource) {
        return new RegisterVehicleCommand(
                resource.carrierId(),
                resource.vehicleTypeId(),
                resource.licensePlate(),
                resource.brand(),
                resource.model(),
                resource.manufactureYear(),
                resource.payloadKg());
    }
}
