package com.cargolinklabs.loadmatch.platform.rating.domain.repositories;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.aggregates.Rating;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RatingId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository contract for Rating aggregate
 */
public interface RatingRepository {
    Rating save(Rating rating);

    Optional<Rating> findById(RatingId id);

    boolean existsByTripIdAndRaterId(UUID tripId, UUID raterId);

    List<Rating> findAllByRateeId(UUID rateeId);
}

