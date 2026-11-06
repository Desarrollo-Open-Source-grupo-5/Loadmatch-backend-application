package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

import java.util.UUID;

/**
 * Carrier identifier value object
 *
 * @param carrierId the UUID that identifies the carrier
 */
public record CarrierId(UUID carrierId) {
    public CarrierId {
        if (carrierId == null) {
            throw new IllegalArgumentException("Carrier id cannot be null");
        }
    }

    /**
     * Creates a new random carrier identifier
     */
    public CarrierId() {
        this(UUID.randomUUID());
    }
}
