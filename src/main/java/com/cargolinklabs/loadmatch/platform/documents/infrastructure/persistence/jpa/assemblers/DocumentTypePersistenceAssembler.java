package com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.DocumentType;
import com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.entities.DocumentTypePersistenceEntity;

public final class DocumentTypePersistenceAssembler {
    private DocumentTypePersistenceAssembler() {
    }

    public static DocumentType toDomainFromPersistence(DocumentTypePersistenceEntity entity) {
        return new DocumentType(entity.getId(), entity.getCode(), entity.getName(),
                entity.isRequiresExpiration(), entity.isMandatory());
    }
}
