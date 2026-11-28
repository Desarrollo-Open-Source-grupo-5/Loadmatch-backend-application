package com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates;

import lombok.Getter;

import java.util.UUID;

@Getter
public class DocumentType {
    private final UUID id;
    private final String code;
    private final String name;
    private final boolean requiresExpiration;
    private final boolean mandatory;

    public DocumentType(UUID id, String code, String name, boolean requiresExpiration, boolean mandatory) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.requiresExpiration = requiresExpiration;
        this.mandatory = mandatory;
    }
}
