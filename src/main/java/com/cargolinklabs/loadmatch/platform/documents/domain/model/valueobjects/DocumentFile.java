package com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects;

public record DocumentFile(String fileName, String contentType, byte[] bytes) {
}
