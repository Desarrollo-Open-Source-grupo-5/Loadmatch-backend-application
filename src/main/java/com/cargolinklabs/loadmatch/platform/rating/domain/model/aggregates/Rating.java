package com.cargolinklabs.loadmatch.platform.rating.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.events.RatingRegisteredEvent;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.Comment;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RaterType;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RatingId;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.Score;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.util.UUID;

/**
 * Rating aggregate root
 */
@Getter
public class Rating extends AbstractDomainAggregateRoot<Rating> {
    private RatingId id;
    private UUID tripId;
    private UUID raterId;
    private UUID rateeId;
    private RaterType raterType;
    private Score score;
    private Comment comment;

    public Rating(UUID tripId, UUID raterId, UUID rateeId, RaterType raterType, Score score, Comment comment) {
        if (tripId == null) {
            throw new IllegalArgumentException("Trip id cannot be null");
        }
        if (raterId == null) {
            throw new IllegalArgumentException("Rater id cannot be null");
        }
        if (rateeId == null) {
            throw new IllegalArgumentException("Ratee id cannot be null");
        }
        if (raterId.equals(rateeId)) {
            throw new IllegalArgumentException("Rater and ratee cannot be the same");
        }
        if (raterType == null) {
            throw new IllegalArgumentException("Rater type cannot be null");
        }
        if (score == null) {
            throw new IllegalArgumentException("Score cannot be null");
        }
        this.id = new RatingId();
        this.tripId = tripId;
        this.raterId = raterId;
        this.rateeId = rateeId;
        this.raterType = raterType;
        this.score = score;
        this.comment = comment;
    }

    public static Rating reconstitute(RatingId id, UUID tripId, UUID raterId, UUID rateeId,
                                      RaterType raterType, Score score, Comment comment) {
        var rating = new Rating(tripId, raterId, rateeId, raterType, score, comment);
        rating.id = id;
        return rating;
    }

    public void onRegistered() {
        this.registerDomainEvent(new RatingRegisteredEvent(
                this.id.ratingId(),
                this.tripId,
                this.raterId,
                this.rateeId,
                this.raterType,
                this.score.value()
        ));
    }
}

