package com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects;

import java.util.UUID;

/**
 * Identifier of the vehicle used in the trip (reference by identity to Fleet)
 *
 * @param vehicleId the UUID identifier
 */
public record VehicleId(UUID vehicleId) {
    public VehicleId {
        if (vehicleId == null) {
            throw new IllegalArgumentException("VehicleId cannot be null");
        }
    }
}
