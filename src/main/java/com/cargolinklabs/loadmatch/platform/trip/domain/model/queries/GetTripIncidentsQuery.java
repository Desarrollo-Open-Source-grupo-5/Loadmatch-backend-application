package com.cargolinklabs.loadmatch.platform.trip.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;

public record GetTripIncidentsQuery(TripId tripId, String requesterEmail) {
}
