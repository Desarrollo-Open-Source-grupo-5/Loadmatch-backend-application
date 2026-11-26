package com.cargolinklabs.loadmatch.platform.trip.domain.services;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.IncidentEvidence;

import java.util.Optional;
import java.util.UUID;

public interface EvidenceStorageService {
    void store(UUID incidentId, IncidentEvidence evidence);

    Optional<IncidentEvidence> findByIncidentId(UUID incidentId);
}
