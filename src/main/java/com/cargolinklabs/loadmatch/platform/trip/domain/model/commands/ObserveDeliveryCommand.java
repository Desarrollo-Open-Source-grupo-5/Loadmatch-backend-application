package com.cargolinklabs.loadmatch.platform.trip.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;

/**
 * Command used by the shipper to register an observation about the delivery
 *
 * @param tripId the trip identifier
 * @param observation the reason of the observation
 */
public record ObserveDeliveryCommand(TripId tripId, String observation) {
    public ObserveDeliveryCommand {
        if (tripId == null) {
            throw new IllegalArgumentException("tripId cannot be null");
        }
        if (observation == null || observation.isBlank()) {
            throw new IllegalArgumentException("observation cannot be null or blank");
        }
    }
}
