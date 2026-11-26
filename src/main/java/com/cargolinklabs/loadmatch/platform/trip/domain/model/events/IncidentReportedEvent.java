package com.cargolinklabs.loadmatch.platform.trip.domain.model.events;

import java.util.UUID;

public record IncidentReportedEvent(UUID tripId, UUID shipperId, UUID carrierId, String type) {
}
