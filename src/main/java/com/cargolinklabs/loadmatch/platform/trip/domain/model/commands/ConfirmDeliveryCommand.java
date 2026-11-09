package com.cargolinklabs.loadmatch.platform.trip.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;

/**
 * Command used by the shipper to confirm that the cargo was received as agreed
 *
 * @param tripId the trip identifier
 */
public record ConfirmDeliveryCommand(TripId tripId) {
    public ConfirmDeliveryCommand {
        if (tripId == null) {
            throw new IllegalArgumentException("tripId cannot be null");
        }
    }
}
