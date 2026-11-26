package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.UUID;

public record IncidentResource(UUID id, String type, String description, String evidenceUrl, LocalDateTime reportedAt) {
}
