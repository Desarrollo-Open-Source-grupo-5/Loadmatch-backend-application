package com.cargolinklabs.loadmatch.platform.rating.domain.model.events;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RaterType;

import java.util.UUID;

/**
 * Domain event published when a new rating is registered
 */
public record RatingRegisteredEvent(
        UUID ratingId,
        UUID tripId,
        UUID raterId,
        UUID rateeId,
        RaterType raterType,
        int score
) {
}

