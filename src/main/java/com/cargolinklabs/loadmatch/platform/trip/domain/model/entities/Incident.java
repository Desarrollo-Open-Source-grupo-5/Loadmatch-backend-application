package com.cargolinklabs.loadmatch.platform.trip.domain.model.entities;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.IncidentType;

import java.time.LocalDateTime;
import java.util.UUID;

public record Incident(UUID id, IncidentType type, String description, String evidenceUrl, LocalDateTime reportedAt) {
    public Incident {
        if (id == null || type == null || evidenceUrl == null || evidenceUrl.isBlank() || reportedAt == null) {
            throw new IllegalArgumentException("An incident requires its identifier, type, evidence and report time");
        }
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("The incident description must contain at most 500 characters");
        }
        description = description == null || description.isBlank() ? null : description.trim();
    }
}
