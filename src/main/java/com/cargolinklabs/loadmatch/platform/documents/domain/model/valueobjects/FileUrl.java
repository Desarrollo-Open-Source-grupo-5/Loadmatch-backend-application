package com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects;

public record FileUrl(String url) {
    public FileUrl {
        if (url == null || url.isBlank()) throw new IllegalArgumentException("File url cannot be blank");
    }
}
