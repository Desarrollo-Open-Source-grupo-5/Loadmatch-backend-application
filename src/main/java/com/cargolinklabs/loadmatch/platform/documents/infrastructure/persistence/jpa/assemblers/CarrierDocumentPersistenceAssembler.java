package com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.CarrierDocument;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentId;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentNumber;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.FileUrl;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.LicensePlateRef;
import com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.entities.CarrierDocumentPersistenceEntity;

public final class CarrierDocumentPersistenceAssembler {
    private CarrierDocumentPersistenceAssembler() {
    }

    public static CarrierDocument toDomainFromPersistence(CarrierDocumentPersistenceEntity entity) {
        return CarrierDocument.restore(new DocumentId(entity.getId()), entity.getCarrierId(),
                entity.getDocumentTypeId(), new DocumentNumber(entity.getDocumentNumber()),
                new LicensePlateRef(entity.getLicensePlate()), new FileUrl(entity.getFileUrl()),
                entity.getFileName(), entity.getExpiresAt(), entity.getStatus(), entity.getRejectionReason(),
                entity.getUploadedAt(), entity.getValidatedAt());
    }

    public static CarrierDocumentPersistenceEntity toPersistenceFromDomain(CarrierDocument document) {
        var entity = new CarrierDocumentPersistenceEntity();
        entity.setId(document.getId().documentId());
        entity.setCarrierId(document.getCarrierId());
        entity.setDocumentTypeId(document.getDocumentTypeId());
        entity.setDocumentNumber(document.getDocumentNumber().value());
        entity.setLicensePlate(document.getLicensePlate().plate());
        entity.setFileName(document.getFileName());
        entity.setFileUrl(document.getFileUrl().url());
        entity.setExpiresAt(document.getExpiresAt());
        entity.setStatus(document.getStatus());
        entity.setRejectionReason(document.getRejectionReason());
        entity.setUploadedAt(document.getUploadedAt());
        entity.setValidatedAt(document.getValidatedAt());
        return entity;
    }
}
