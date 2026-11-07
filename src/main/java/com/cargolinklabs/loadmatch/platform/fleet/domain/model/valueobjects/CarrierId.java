package com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects;

import java.util.UUID;

/**
 * Identifier of the carrier that owns a vehicle (reference by identity to Profiles)
 *
 * @param carrierId the UUID identifier
 */
public record CarrierId(UUID carrierId) {
    public CarrierId {
        if (carrierId == null) {
            throw new IllegalArgumentException("CarrierId cannot be null");
        }
    }
}
