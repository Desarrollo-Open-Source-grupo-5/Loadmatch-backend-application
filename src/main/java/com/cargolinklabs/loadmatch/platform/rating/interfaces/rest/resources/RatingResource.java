package com.cargolinklabs.loadmatch.platform.rating.interfaces.rest.resources;

import java.util.UUID;

/**
 * Rating response resource
 */
public record RatingResource(
        UUID id,
        UUID tripId,
        UUID raterId,
        UUID rateeId,
        String raterType,
        int score,
        String comment
) {
}

