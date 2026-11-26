package com.cargolinklabs.loadmatch.platform.trip.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.IncidentType;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;

public record ReportIncidentCommand(TripId tripId, String reporterEmail, IncidentType type, String description,
                                    String fileName, String contentType, String fileBase64) {
}
