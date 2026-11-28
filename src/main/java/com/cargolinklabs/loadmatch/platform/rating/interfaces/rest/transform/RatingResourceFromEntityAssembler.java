package com.cargolinklabs.loadmatch.platform.rating.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.aggregates.Rating;
import com.cargolinklabs.loadmatch.platform.rating.interfaces.rest.resources.RatingResource;

/**
 * Assembler translating Rating aggregate to RatingResource
 */
public final class RatingResourceFromEntityAssembler {

    private RatingResourceFromEntityAssembler() {
    }

    public static RatingResource toResourceFromEntity(Rating entity) {
        return new RatingResource(
                entity.getId().ratingId(),
                entity.getTripId(),
                entity.getRaterId(),
                entity.getRateeId(),
                entity.getRaterType().name(),
                entity.getScore().value(),
                entity.getComment() != null ? entity.getComment().text() : null
        );
    }
}

