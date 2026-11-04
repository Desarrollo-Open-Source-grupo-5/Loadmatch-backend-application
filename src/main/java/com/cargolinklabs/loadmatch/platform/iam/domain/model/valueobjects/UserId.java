package com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects;

import java.util.UUID;

/**
 * User identifier value object
 *
 * @param userId the UUID that identifies the user
 */
public record UserId(UUID userId) {
    public UserId {
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
    }

    /**
     * Creates a new random user identifier
     */
    public UserId() {
        this(UUID.randomUUID());
    }
}
