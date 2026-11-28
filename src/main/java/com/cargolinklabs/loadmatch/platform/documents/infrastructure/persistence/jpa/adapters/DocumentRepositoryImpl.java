package com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.CarrierDocument;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentFile;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentId;
import com.cargolinklabs.loadmatch.platform.documents.domain.repositories.DocumentRepository;
import com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.assemblers.CarrierDocumentPersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.entities.DocumentFilePersistenceEntity;
import com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.repositories.CarrierDocumentPersistenceRepository;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.events.DomainEventPublisher;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DocumentRepositoryImpl implements DocumentRepository {
    private final CarrierDocumentPersistenceRepository persistence;
    private final EntityManager entityManager;
    private final DomainEventPublisher eventPublisher;

    public DocumentRepositoryImpl(CarrierDocumentPersistenceRepository persistence, EntityManager entityManager,
                                  DomainEventPublisher eventPublisher) {
        this.persistence = persistence;
        this.entityManager = entityManager;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void lockUploadsForCarrier(UUID carrierId) {
        // Serialize the readiness check, even when this is the carrier's first upload.
        entityManager.createNativeQuery("select 1 from pg_advisory_xact_lock(hashtextextended(:lockKey, 0))")
                .setParameter("lockKey", "documents:" + carrierId)
                .getSingleResult();
    }

    @Override
    public List<CarrierDocument> findByCarrierId(UUID carrierId) {
        return persistence.findByCarrierIdOrderByUploadedAtDescIdDesc(carrierId).stream()
                .map(CarrierDocumentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<CarrierDocument> findById(DocumentId id) {
        return persistence.findById(id.documentId()).map(CarrierDocumentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<DocumentFile> findFileById(DocumentId id) {
        return Optional.ofNullable(entityManager.find(DocumentFilePersistenceEntity.class, id.documentId()))
                .map(file -> new DocumentFile(file.getDocument().getFileName(), file.getContentType(), file.getContent()));
    }

    @Override
    public CarrierDocument save(CarrierDocument document, DocumentFile file) {
        var entity = CarrierDocumentPersistenceAssembler.toPersistenceFromDomain(document);
        entityManager.persist(entity);
        entityManager.persist(new DocumentFilePersistenceEntity(entity, file.contentType(), file.bytes()));
        eventPublisher.publishAndClearEvents(document);
        return document;
    }
}
