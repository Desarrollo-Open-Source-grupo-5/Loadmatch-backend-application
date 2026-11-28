package com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.DocumentType;
import com.cargolinklabs.loadmatch.platform.documents.domain.repositories.DocumentTypeRepository;
import com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.assemblers.DocumentTypePersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.repositories.DocumentTypePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DocumentTypeRepositoryImpl implements DocumentTypeRepository {
    private final DocumentTypePersistenceRepository persistence;

    public DocumentTypeRepositoryImpl(DocumentTypePersistenceRepository persistence) {
        this.persistence = persistence;
    }

    @Override
    public Optional<DocumentType> findById(UUID id) {
        return persistence.findById(id).map(DocumentTypePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<DocumentType> findAll() {
        return persistence.findAllByOrderByCodeAsc().stream()
                .map(DocumentTypePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public void insertIfAbsent(DocumentType documentType) {
        persistence.insertIfAbsent(documentType.getId(), documentType.getCode(), documentType.getName(),
                documentType.isRequiresExpiration(), documentType.isMandatory());
    }
}
