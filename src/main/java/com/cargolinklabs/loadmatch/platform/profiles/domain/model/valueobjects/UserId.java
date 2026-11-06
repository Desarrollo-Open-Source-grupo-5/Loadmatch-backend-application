package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

import java.util.UUID;

/**
 * Identifier of the IAM user that owns a profile
 * <p>
 *     Profiles references the IAM user by identity only.
 * </p>
 *
 * @param userId the UUID of the IAM user
 */
public record UserId(UUID userId) {
    public UserId {
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
    }
}
