package com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.DocumentType;
import com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.resources.DocumentTypeResource;

public final class DocumentTypeResourceFromEntityAssembler {
    private DocumentTypeResourceFromEntityAssembler() {
    }

    public static DocumentTypeResource toResourceFromEntity(DocumentType type) {
        return new DocumentTypeResource(type.getId(), type.getCode(), type.getName(),
                type.isMandatory(), type.isRequiresExpiration());
    }
}
