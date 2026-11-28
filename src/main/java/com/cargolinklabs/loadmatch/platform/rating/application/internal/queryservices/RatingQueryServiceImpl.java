package com.cargolinklabs.loadmatch.platform.rating.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.rating.application.queryservices.RatingQueryService;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.aggregates.Rating;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.queries.GetRatingsByRateeIdQuery;
import com.cargolinklabs.loadmatch.platform.rating.domain.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Query service implementation for Rating
 */
@Service
public class RatingQueryServiceImpl implements RatingQueryService {
    private final RatingRepository ratingRepository;

    public RatingQueryServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<Rating> handle(GetRatingsByRateeIdQuery query) {
        return ratingRepository.findAllByRateeId(query.rateeId());
    }
}

