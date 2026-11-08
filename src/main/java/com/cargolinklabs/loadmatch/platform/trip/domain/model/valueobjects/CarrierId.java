package com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects;

import java.util.UUID;

/**
 * Identifier of the carrier that performs the trip (reference by identity to Profiles)
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
