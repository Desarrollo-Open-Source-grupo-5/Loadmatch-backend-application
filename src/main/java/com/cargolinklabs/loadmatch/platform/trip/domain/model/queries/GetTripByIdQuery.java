package com.cargolinklabs.loadmatch.platform.trip.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;

/**
 * Query to get a trip by its identifier
 *
 * @param tripId the trip identifier
 */
public record GetTripByIdQuery(TripId tripId) {
}
