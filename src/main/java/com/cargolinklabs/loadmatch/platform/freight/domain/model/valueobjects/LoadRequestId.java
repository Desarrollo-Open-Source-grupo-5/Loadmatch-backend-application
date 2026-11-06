package com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects;

import java.util.UUID;

/**
 * Load request identifier value object
 *
 * @param loadRequestId the UUID identifier
 */
public record LoadRequestId(UUID loadRequestId) {
    public LoadRequestId {
        if (loadRequestId == null) {
            throw new IllegalArgumentException("LoadRequestId cannot be null");
        }
    }

    /**
     * Creates a new random identifier
     */
    public LoadRequestId() {
        this(UUID.randomUUID());
    }
}
