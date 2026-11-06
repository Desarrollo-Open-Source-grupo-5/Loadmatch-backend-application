package com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects;

import java.util.UUID;

/**
 * Identifier of the vehicle type required by a load request (reference by identity to Fleet)
 *
 * @param vehicleTypeId the UUID identifier
 */
public record VehicleTypeId(UUID vehicleTypeId) {
    public VehicleTypeId {
        if (vehicleTypeId == null) {
            throw new IllegalArgumentException("VehicleTypeId cannot be null");
        }
    }
}
