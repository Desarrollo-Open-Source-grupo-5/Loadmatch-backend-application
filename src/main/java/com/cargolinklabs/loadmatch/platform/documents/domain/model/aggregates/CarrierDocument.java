package com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.events.DocumentValidatedEvent;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.UUID;

@Getter
public class CarrierDocument extends AbstractDomainAggregateRoot<CarrierDocument> {
    private static final ZoneId VALIDATION_ZONE = ZoneId.of("America/Lima");

    private DocumentId id;
    private UUID carrierId;
    private UUID documentTypeId;
    private DocumentNumber documentNumber;
    private LicensePlateRef licensePlate;
    private FileUrl fileUrl;
    private String fileName;
    private ValidationStatus status;
    private String rejectionReason;
    private LocalDate expiresAt;
    private LocalDateTime uploadedAt;
    private LocalDateTime validatedAt;

    public CarrierDocument(UUID carrierId, UUID documentTypeId, DocumentNumber documentNumber,
                           LicensePlateRef licensePlate, LocalDate expiresAt, String fileName) {
        this.id = new DocumentId();
        this.carrierId = carrierId;
        this.documentTypeId = documentTypeId;
        this.documentNumber = documentNumber;
        this.licensePlate = licensePlate;
        this.fileUrl = new FileUrl("/api/v1/documents/" + id.documentId() + "/file");
        this.fileName = fileName;
        this.expiresAt = expiresAt;
        this.status = ValidationStatus.PENDING;
        this.uploadedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    public static CarrierDocument restore(DocumentId id, UUID carrierId, UUID documentTypeId,
                                          DocumentNumber documentNumber, LicensePlateRef licensePlate, FileUrl fileUrl,
                                          String fileName, LocalDate expiresAt, ValidationStatus status,
                                          String rejectionReason, LocalDateTime uploadedAt, LocalDateTime validatedAt) {
        return new CarrierDocument(id, carrierId, documentTypeId, documentNumber, licensePlate, fileUrl,
                fileName, expiresAt, status, rejectionReason, uploadedAt, validatedAt);
    }

    private CarrierDocument(DocumentId id, UUID carrierId, UUID documentTypeId, DocumentNumber documentNumber,
                            LicensePlateRef licensePlate, FileUrl fileUrl, String fileName, LocalDate expiresAt,
                            ValidationStatus status, String rejectionReason, LocalDateTime uploadedAt,
                            LocalDateTime validatedAt) {
        this.id = id;
        this.carrierId = carrierId;
        this.documentTypeId = documentTypeId;
        this.documentNumber = documentNumber;
        this.licensePlate = licensePlate;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.expiresAt = expiresAt;
        this.status = status;
        this.rejectionReason = rejectionReason;
        this.uploadedAt = uploadedAt;
        this.validatedAt = validatedAt;
    }

    public static LocalDate validationDate() {
        return LocalDate.now(VALIDATION_ZONE);
    }

    public ValidationStatus statusOn(LocalDate date) {
        if (status == ValidationStatus.APPROVED && expiresAt != null && expiresAt.isBefore(date)) {
            return ValidationStatus.EXPIRED;
        }
        return status;
    }

    public boolean isApprovedOn(LocalDate date) {
        return statusOn(date) == ValidationStatus.APPROVED;
    }

    public void approve(boolean allMandatoryApproved) {
        this.status = ValidationStatus.APPROVED;
        this.rejectionReason = null;
        this.validatedAt = LocalDateTime.now(ZoneOffset.UTC);
        registerDomainEvent(new DocumentValidatedEvent(id.documentId(), carrierId, status.name(), allMandatoryApproved));
    }

    public void reject(String reason) {
        this.status = ValidationStatus.REJECTED;
        this.rejectionReason = reason;
        this.validatedAt = LocalDateTime.now(ZoneOffset.UTC);
        registerDomainEvent(new DocumentValidatedEvent(id.documentId(), carrierId, status.name(), false));
    }
}
