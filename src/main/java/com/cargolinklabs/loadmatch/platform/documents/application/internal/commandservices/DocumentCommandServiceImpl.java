package com.cargolinklabs.loadmatch.platform.documents.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.documents.application.commandservices.DocumentCommandService;
import com.cargolinklabs.loadmatch.platform.documents.application.internal.outboundservices.acl.DocumentAccessService;
import com.cargolinklabs.loadmatch.platform.documents.application.internal.outboundservices.acl.ExternalFleetService;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.CarrierDocument;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.DocumentType;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.commands.SeedDocumentTypesCommand;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.commands.UploadDocumentCommand;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentFile;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentNumber;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.LicensePlateRef;
import com.cargolinklabs.loadmatch.platform.documents.domain.repositories.DocumentRepository;
import com.cargolinklabs.loadmatch.platform.documents.domain.repositories.DocumentTypeRepository;
import com.cargolinklabs.loadmatch.platform.documents.domain.services.FileStorageService;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentCommandServiceImpl implements DocumentCommandService {
    private final DocumentTypeRepository documentTypes;
    private final DocumentRepository documents;
    private final FileStorageService fileStorage;
    private final ExternalFleetService fleet;
    private final DocumentAccessService access;

    public DocumentCommandServiceImpl(DocumentTypeRepository documentTypes, DocumentRepository documents,
                                      FileStorageService fileStorage, ExternalFleetService fleet,
                                      DocumentAccessService access) {
        this.documentTypes = documentTypes;
        this.documents = documents;
        this.fileStorage = fileStorage;
        this.fleet = fleet;
        this.access = access;
    }

    @Override
    @Transactional
    public Result<CarrierDocument, ApplicationError> handle(UploadDocumentCommand command, String email) {
        if (!access.isOwner(command.carrierId(), email)) {
            return Result.failure(ApplicationError.notFound("carrier", command.carrierId().toString()));
        }
        var documentType = documentTypes.findById(command.documentTypeId());
        if (documentType.isEmpty()) {
            return Result.failure(ApplicationError.notFound("document-type", command.documentTypeId().toString()));
        }

        DocumentFile file;
        try {
            file = new DocumentFile(command.fileName(), command.contentType(),
                    fileStorage.decodeAndValidate(command.contentType(), command.fileBase64()));
        } catch (IllegalArgumentException exception) {
            return Result.failure(ApplicationError.validationError("document-file", exception.getMessage()));
        }

        documents.lockUploadsForCarrier(command.carrierId());
        var type = documentType.get();
        var document = new CarrierDocument(command.carrierId(), type.getId(),
                new DocumentNumber(command.documentNumber()), new LicensePlateRef(command.licensePlate()),
                command.expiresAt(), file.fileName());

        var today = CarrierDocument.validationDate();
        var rejectionReason = validationError(document, type, today);
        if (rejectionReason != null) {
            document.reject(rejectionReason);
        } else {
            document.approve(allMandatoryApproved(document, today));
        }
        return Result.success(documents.save(document, file));
    }

    @Override
    @Transactional
    public void handle(SeedDocumentTypesCommand command) {
        List.of(catalogType("DRIVER_LICENSE", "Driver license", true),
                        catalogType("SOAT", "SOAT", true),
                        catalogType("VEHICLE_REGISTRATION", "Vehicle registration card", false))
                .forEach(documentTypes::insertIfAbsent);
    }

    private static DocumentType catalogType(String code, String name, boolean requiresExpiration) {
        var id = UUID.nameUUIDFromBytes(("loadmatch:document-type:" + code).getBytes(StandardCharsets.UTF_8));
        return new DocumentType(id, code, name, requiresExpiration, true);
    }

    private String validationError(CarrierDocument document, DocumentType type, LocalDate today) {
        if (type.isRequiresExpiration() && document.getExpiresAt() == null) {
            return "EXPIRATION_REQUIRED";
        }
        if (document.getExpiresAt() != null && document.getExpiresAt().isBefore(today)) {
            return "DOCUMENT_EXPIRED";
        }
        if (List.of("SOAT", "VEHICLE_REGISTRATION").contains(type.getCode())
                && !fleet.hasActivePlate(document.getCarrierId(), document.getLicensePlate().plate())) {
            return "PLATE_MISMATCH";
        }
        return null;
    }

    private boolean allMandatoryApproved(CarrierDocument uploaded, LocalDate today) {
        var latestByType = new LinkedHashMap<UUID, CarrierDocument>();
        documents.findByCarrierId(uploaded.getCarrierId()).forEach(
                document -> latestByType.putIfAbsent(document.getDocumentTypeId(), document));
        return documentTypes.findAll().stream()
                .filter(DocumentType::isMandatory)
                .allMatch(type -> {
                    if (type.getId().equals(uploaded.getDocumentTypeId())) {
                        return true;
                    }
                    var latest = latestByType.get(type.getId());
                    return latest != null && latest.isApprovedOn(today);
                });
    }
}
