package com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects;

import java.util.UUID;

/**
 * Vehicle identifier value object
 *
 * @param vehicleId the UUID identifier
 */
public record VehicleId(UUID vehicleId) {
    public VehicleId {
        if (vehicleId == null) {
            throw new IllegalArgumentException("VehicleId cannot be null");
        }
    }

    /**
     * Creates a new random identifier
     */
    public VehicleId() {
        this(UUID.randomUUID());
    }
}
