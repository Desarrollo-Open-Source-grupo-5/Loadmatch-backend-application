package com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects;

public record DocumentNumber(String value) {
    public DocumentNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Document number cannot be blank");
        }
        value = value.trim();
    }
}
