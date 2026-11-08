package com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects;

import java.util.UUID;

/**
 * Identifier of the shipper that owns the load (reference by identity to Profiles)
 *
 * @param shipperId the UUID identifier
 */
public record ShipperId(UUID shipperId) {
    public ShipperId {
        if (shipperId == null) {
            throw new IllegalArgumentException("ShipperId cannot be null");
        }
    }
}
