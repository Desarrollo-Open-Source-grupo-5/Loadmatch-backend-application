package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.ReportIncidentCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.IncidentType;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.ReportIncidentResource;

import java.util.UUID;

public final class ReportIncidentCommandFromResourceAssembler {
    private ReportIncidentCommandFromResourceAssembler() {
    }

    public static ReportIncidentCommand toCommandFromResource(UUID tripId, String reporterEmail,
                                                              ReportIncidentResource resource) {
        return new ReportIncidentCommand(new TripId(tripId), reporterEmail, IncidentType.valueOf(resource.type()),
                resource.description(), resource.fileName(), resource.contentType(), resource.fileBase64());
    }
}
