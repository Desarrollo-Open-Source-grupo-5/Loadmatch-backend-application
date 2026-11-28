package com.cargolinklabs.loadmatch.platform.rating.interfaces.rest.resources;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RaterType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Request resource to register a rating
 */
public record RegisterRatingResource(
        @NotNull(message = "{validation.not-blank}") UUID tripId,
        @NotNull(message = "{validation.not-blank}") UUID raterId,
        @NotNull(message = "{validation.not-blank}") RaterType raterType,
        @Min(1) @Max(5) int score,
        @Size(max = 500) String comment
) {
}

