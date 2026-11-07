package com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleId;

/**
 * Query to get a vehicle by its identifier
 *
 * @param vehicleId the vehicle identifier
 */
public record GetVehicleByIdQuery(VehicleId vehicleId) {
}
