package com.cargolinklabs.loadmatch.platform.trip.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;

import java.util.UUID;

public record GetIncidentEvidenceQuery(TripId tripId, UUID incidentId, String requesterEmail) {
}
