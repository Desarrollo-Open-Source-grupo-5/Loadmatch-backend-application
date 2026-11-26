package com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.IncidentEvidence;
import com.cargolinklabs.loadmatch.platform.trip.domain.services.EvidenceStorageService;
import com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.entities.IncidentEvidencePersistenceEntity;
import com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.entities.IncidentPersistenceEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaEvidenceStorageService implements EvidenceStorageService {
    private final EntityManager entityManager;

    public JpaEvidenceStorageService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void store(UUID incidentId, IncidentEvidence evidence) {
        var incident = entityManager.getReference(IncidentPersistenceEntity.class, incidentId);
        entityManager.persist(new IncidentEvidencePersistenceEntity(incident, evidence.fileName(),
                evidence.contentType(), evidence.content()));
    }

    @Override
    public Optional<IncidentEvidence> findByIncidentId(UUID incidentId) {
        return Optional.ofNullable(entityManager.find(IncidentEvidencePersistenceEntity.class, incidentId))
                .map(entity -> new IncidentEvidence(entity.getFileName(), entity.getContentType(), entity.getContent()));
    }
}
