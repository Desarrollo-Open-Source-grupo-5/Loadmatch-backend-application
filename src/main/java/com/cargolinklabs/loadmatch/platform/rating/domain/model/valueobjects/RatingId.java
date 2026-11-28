package com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects;

import java.util.UUID;

/**
 * Rating identifier value object
 */
public record RatingId(UUID ratingId) {

    public RatingId() {
        this(UUID.randomUUID());
    }

    public RatingId {
        if (ratingId == null) {
            throw new IllegalArgumentException("Rating id cannot be null");
        }
    }
}

