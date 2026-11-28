package com.cargolinklabs.loadmatch.platform.rating.application.queryservices;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.aggregates.Rating;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.queries.GetRatingsByRateeIdQuery;

import java.util.List;

/**
 * Query service interface for Rating
 */
public interface RatingQueryService {
    List<Rating> handle(GetRatingsByRateeIdQuery query);
}

