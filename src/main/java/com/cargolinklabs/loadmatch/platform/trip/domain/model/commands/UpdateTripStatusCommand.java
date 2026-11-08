package com.cargolinklabs.loadmatch.platform.trip.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripStatus;

/**
 * Command used by the carrier to update the status of a trip
 *
 * @param tripId the trip identifier
 * @param status the new status
 * @param description the description of the update
 * @param evidenceUrl the URL of the delivery evidence (required for DELIVERED)
 */
public record UpdateTripStatusCommand(TripId tripId, TripStatus status, String description, String evidenceUrl) {
    public UpdateTripStatusCommand {
        if (tripId == null || status == null) {
            throw new IllegalArgumentException("tripId and status cannot be null");
        }
    }
}
