package com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record UploadDocumentResource(
        @NotNull UUID carrierId,
        @NotNull UUID documentTypeId,
        @NotBlank @Size(max = 100) String documentNumber,
        @Size(max = 20) String licensePlate,
        @JsonAlias("expirationDate") LocalDate expiresAt,
        @NotBlank @Size(max = 255) @Pattern(regexp = "[^\\p{Cntrl}/\\\\]+") String fileName,
        @NotBlank @Size(max = 50) String contentType,
        @NotBlank String fileBase64
) {
}
