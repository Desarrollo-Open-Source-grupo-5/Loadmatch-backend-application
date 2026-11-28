package com.cargolinklabs.loadmatch.platform.documents.domain.model.commands;

import java.time.LocalDate;
import java.util.UUID;

public record UploadDocumentCommand(UUID carrierId, UUID documentTypeId, String documentNumber,
                                    String licensePlate, LocalDate expiresAt,
                                    String fileName, String contentType, String fileBase64) {
}
