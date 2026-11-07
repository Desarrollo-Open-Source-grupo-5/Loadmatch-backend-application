package com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleId;

import java.util.UUID;

/**
 * Command to update the technical data of a vehicle
 */
public record UpdateVehicleCommand(
        VehicleId vehicleId,
        UUID vehicleTypeId,
        String brand,
        String model,
        int manufactureYear,
        double payloadKg) {

    public UpdateVehicleCommand {
        if (vehicleId == null || vehicleTypeId == null) {
            throw new IllegalArgumentException("vehicleId and vehicleTypeId cannot be null");
        }
    }
}
