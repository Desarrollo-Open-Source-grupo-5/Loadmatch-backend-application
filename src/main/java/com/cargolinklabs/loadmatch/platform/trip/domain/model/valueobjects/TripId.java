package com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects;

import java.util.UUID;

/**
 * Trip identifier value object
 *
 * @param tripId the UUID identifier
 */
public record TripId(UUID tripId) {
    public TripId {
        if (tripId == null) {
            throw new IllegalArgumentException("TripId cannot be null");
        }
    }

    /**
     * Creates a new random identifier
     */
    public TripId() {
        this(UUID.randomUUID());
    }
}
