package com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.CarrierDocument;
import com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.resources.DocumentResource;

import java.time.ZoneOffset;

public final class DocumentResourceFromEntityAssembler {
    private DocumentResourceFromEntityAssembler() {
    }

    public static DocumentResource toResourceFromEntity(CarrierDocument document) {
        var reason = document.getRejectionReason();
        return new DocumentResource(document.getId().documentId(), document.getCarrierId(),
                document.getDocumentTypeId(), document.getDocumentNumber().value(), document.getLicensePlate().plate(),
                document.getFileName(), document.getFileUrl().url(), document.statusOn(CarrierDocument.validationDate()),
                reason, descriptionOf(reason), null, document.getExpiresAt(), document.getUploadedAt().atOffset(ZoneOffset.UTC),
                document.getValidatedAt() == null ? null : document.getValidatedAt().atOffset(ZoneOffset.UTC));
    }

    private static String descriptionOf(String reason) {
        return switch (reason) {
            case null -> null;
            case "EXPIRATION_REQUIRED" -> "An expiration date is required for this document type";
            case "DOCUMENT_EXPIRED" -> "The document has already expired";
            case "PLATE_MISMATCH" -> "The license plate must belong to an active vehicle of the carrier";
            default -> reason;
        };
    }
}
