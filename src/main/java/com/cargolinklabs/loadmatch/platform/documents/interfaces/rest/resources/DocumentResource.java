package com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.resources;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.ValidationStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record DocumentResource(
        UUID id, UUID carrierId, UUID documentTypeId, String documentNumber, String licensePlate,
        String fileName, String fileUrl, ValidationStatus validationStatus, String rejectionReasonCode,
        String rejectionReasonDescription, LocalDate issueDate, LocalDate expirationDate,
        OffsetDateTime uploadedAt, OffsetDateTime validatedAt
) {
}
