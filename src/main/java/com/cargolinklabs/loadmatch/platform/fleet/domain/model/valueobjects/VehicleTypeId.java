package com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects;

import java.util.UUID;

/**
 * Vehicle type identifier value object
 *
 * @param vehicleTypeId the UUID identifier
 */
public record VehicleTypeId(UUID vehicleTypeId) {
    public VehicleTypeId {
        if (vehicleTypeId == null) {
            throw new IllegalArgumentException("VehicleTypeId cannot be null");
        }
    }

    /**
     * Creates a new random identifier
     */
    public VehicleTypeId() {
        this(UUID.randomUUID());
    }
}
