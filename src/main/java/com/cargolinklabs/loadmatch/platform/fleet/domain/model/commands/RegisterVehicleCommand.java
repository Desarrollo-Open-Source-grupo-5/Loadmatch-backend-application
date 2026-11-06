package com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands;

import java.util.UUID;

/**
 * Command to register a vehicle in the fleet of a carrier
 */
public record RegisterVehicleCommand(
        UUID carrierId,
        UUID vehicleTypeId,
        String licensePlate,
        String brand,
        String model,
        int manufactureYear,
        double payloadKg) {

    public RegisterVehicleCommand {
        if (carrierId == null || vehicleTypeId == null) {
            throw new IllegalArgumentException("carrierId and vehicleTypeId cannot be null");
        }
    }
}
