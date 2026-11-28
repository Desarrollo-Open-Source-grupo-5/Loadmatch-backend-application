package com.cargolinklabs.loadmatch.platform.rating.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RaterType;

import java.util.UUID;

/**
 * Command to register a new rating
 */
public record RegisterRatingCommand(
        UUID tripId,
        UUID raterId,
        RaterType raterType,
        int score,
        String comment
) {
}

