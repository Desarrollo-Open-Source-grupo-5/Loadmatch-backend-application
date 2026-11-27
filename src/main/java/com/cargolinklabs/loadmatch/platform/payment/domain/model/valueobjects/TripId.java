package com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects;

import java.util.UUID;

/**
 * Trip identifier value object referenced by the payment
 *
 * @param tripId the UUID identifier
 */
public record TripId(UUID tripId) {
    public TripId {
        if (tripId == null) {
            throw new IllegalArgumentException("TripId cannot be null");
        }
    }
}
