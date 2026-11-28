package com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects;

import java.util.UUID;

public record DocumentId(UUID documentId) {
    public DocumentId {
        if (documentId == null) throw new IllegalArgumentException("Document id cannot be null");
    }

    public DocumentId() {
        this(UUID.randomUUID());
    }
}
