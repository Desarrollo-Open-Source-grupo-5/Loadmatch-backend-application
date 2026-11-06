package com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects;

import java.util.UUID;

/**
 * Identifier of the shipper that publishes a load request (reference by identity to Profiles)
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
