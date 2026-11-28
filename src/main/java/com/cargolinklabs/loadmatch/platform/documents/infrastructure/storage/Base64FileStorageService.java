package com.cargolinklabs.loadmatch.platform.documents.infrastructure.storage;

import com.cargolinklabs.loadmatch.platform.documents.domain.services.FileStorageService;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class Base64FileStorageService implements FileStorageService {
    private static final int MAX_ENCODED_BYTES = ((MAX_BYTES + 2) / 3) * 4;

    @Override
    public byte[] decodeAndValidate(String contentType, String fileBase64) {
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only PDF, JPEG and PNG files are allowed");
        }
        if (fileBase64 == null || fileBase64.isBlank()) {
            throw new IllegalArgumentException("A Base64 file is required");
        }
        if (fileBase64.length() > MAX_ENCODED_BYTES) {
            throw new IllegalArgumentException("The file must be at most 5 MB");
        }

        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(fileBase64);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("The file is not valid Base64", exception);
        }
        if (bytes.length > MAX_BYTES) {
            throw new IllegalArgumentException("The file must be at most 5 MB");
        }
        var matchesDeclaredType = switch (contentType) {
            case "application/pdf" -> startsWith(bytes, 0x25, 0x50, 0x44, 0x46, 0x2d);
            case "image/jpeg" -> startsWith(bytes, 0xff, 0xd8, 0xff);
            case "image/png" -> startsWith(bytes, 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a);
            default -> false;
        };
        if (!matchesDeclaredType) {
            throw new IllegalArgumentException("The file content does not match its declared format");
        }
        return bytes;
    }

    private static boolean startsWith(byte[] bytes, int... signature) {
        if (bytes.length < signature.length) {
            return false;
        }
        for (int i = 0; i < signature.length; i++) {
            if (Byte.toUnsignedInt(bytes[i]) != signature[i]) {
                return false;
            }
        }
        return true;
    }
}
