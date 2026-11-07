package com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleId;

/**
 * Command to deactivate a vehicle
 *
 * @param vehicleId the vehicle identifier
 */
public record DeactivateVehicleCommand(VehicleId vehicleId) {
}
