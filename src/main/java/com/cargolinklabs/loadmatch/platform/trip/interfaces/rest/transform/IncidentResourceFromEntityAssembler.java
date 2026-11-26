package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.entities.Incident;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.IncidentResource;

public final class IncidentResourceFromEntityAssembler {
    private IncidentResourceFromEntityAssembler() {
    }

    public static IncidentResource toResourceFromEntity(Incident incident) {
        return new IncidentResource(incident.id(), incident.type().name(), incident.description(), incident.evidenceUrl(),
                incident.reportedAt());
    }
}
