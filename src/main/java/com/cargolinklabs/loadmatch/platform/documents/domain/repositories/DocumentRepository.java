package com.cargolinklabs.loadmatch.platform.documents.domain.repositories;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.CarrierDocument;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentFile;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository {
    void lockUploadsForCarrier(UUID carrierId);

    List<CarrierDocument> findByCarrierId(UUID carrierId);

    Optional<CarrierDocument> findById(DocumentId id);

    Optional<DocumentFile> findFileById(DocumentId id);

    CarrierDocument save(CarrierDocument document, DocumentFile file);
}
