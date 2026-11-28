package com.cargolinklabs.loadmatch.platform.documents.domain.services;

import java.util.Set;

public interface FileStorageService {
    Set<String> ALLOWED_TYPES = Set.of("application/pdf", "image/jpeg", "image/png");
    int MAX_BYTES = 5 * 1024 * 1024;

    byte[] decodeAndValidate(String contentType, String fileBase64);
}
