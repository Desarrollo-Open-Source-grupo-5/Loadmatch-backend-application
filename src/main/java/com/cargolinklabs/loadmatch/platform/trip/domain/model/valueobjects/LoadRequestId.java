package com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects;

import java.util.UUID;

/**
 * Identifier of the load request transported in a trip (reference by identity to Freight Publishing)
 *
 * @param loadRequestId the UUID identifier
 */
public record LoadRequestId(UUID loadRequestId) {
    public LoadRequestId {
        if (loadRequestId == null) {
            throw new IllegalArgumentException("LoadRequestId cannot be null");
        }
    }
}
