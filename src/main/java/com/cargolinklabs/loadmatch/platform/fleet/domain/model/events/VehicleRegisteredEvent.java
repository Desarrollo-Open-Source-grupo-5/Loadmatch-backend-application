package com.cargolinklabs.loadmatch.platform.fleet.domain.model.events;

import java.util.UUID;

/**
 * Domain event raised when a carrier registers a vehicle
 *
 * @param vehicleId the vehicle identifier
 * @param carrierId the carrier identifier
 * @param licensePlate the license plate
 */
public record VehicleRegisteredEvent(UUID vehicleId, UUID carrierId, String licensePlate) {
}
