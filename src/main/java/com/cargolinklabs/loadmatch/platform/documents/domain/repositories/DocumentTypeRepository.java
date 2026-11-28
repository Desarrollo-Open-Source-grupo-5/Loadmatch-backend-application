package com.cargolinklabs.loadmatch.platform.documents.domain.repositories;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.DocumentType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentTypeRepository {
    Optional<DocumentType> findById(UUID id);

    List<DocumentType> findAll();

    void insertIfAbsent(DocumentType documentType);
}
