package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

import java.util.UUID;

/**
 * Shipper identifier value object
 *
 * @param shipperId the UUID that identifies the shipper
 */
public record ShipperId(UUID shipperId) {
    public ShipperId {
        if (shipperId == null) {
            throw new IllegalArgumentException("Shipper id cannot be null");
        }
    }

    /**
     * Creates a new random shipper identifier
     */
    public ShipperId() {
        this(UUID.randomUUID());
    }
}
