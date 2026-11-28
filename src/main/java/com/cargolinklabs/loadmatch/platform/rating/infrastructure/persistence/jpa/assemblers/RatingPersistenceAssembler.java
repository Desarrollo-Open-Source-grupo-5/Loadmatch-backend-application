package com.cargolinklabs.loadmatch.platform.rating.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.aggregates.Rating;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.Comment;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RatingId;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.Score;
import com.cargolinklabs.loadmatch.platform.rating.infrastructure.persistence.jpa.entities.RatingPersistenceEntity;

/**
 * Assembler translating between Rating domain aggregate and persistence entity
 */
public final class RatingPersistenceAssembler {

    private RatingPersistenceAssembler() {
    }

    public static RatingPersistenceEntity toPersistenceFromDomain(Rating rating) {
        var entity = new RatingPersistenceEntity();
        entity.setId(rating.getId().ratingId());
        entity.setTripId(rating.getTripId());
        entity.setRaterId(rating.getRaterId());
        entity.setRateeId(rating.getRateeId());
        entity.setRaterType(rating.getRaterType());
        entity.setScore(rating.getScore().value());
        entity.setComment(rating.getComment() != null ? rating.getComment().text() : null);
        return entity;
    }

    public static Rating toDomainFromPersistence(RatingPersistenceEntity entity) {
        return Rating.reconstitute(
                new RatingId(entity.getId()),
                entity.getTripId(),
                entity.getRaterId(),
                entity.getRateeId(),
                entity.getRaterType(),
                new Score(entity.getScore()),
                new Comment(entity.getComment())
        );
    }
}

