package com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.commands.UploadDocumentCommand;
import com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.resources.UploadDocumentResource;

public final class UploadDocumentCommandFromResourceAssembler {
    private UploadDocumentCommandFromResourceAssembler() {
    }

    public static UploadDocumentCommand toCommandFromResource(UploadDocumentResource resource) {
        return new UploadDocumentCommand(resource.carrierId(), resource.documentTypeId(),
                resource.documentNumber(), resource.licensePlate(), resource.expiresAt(), resource.fileName(),
                resource.contentType(), resource.fileBase64());
    }
}
