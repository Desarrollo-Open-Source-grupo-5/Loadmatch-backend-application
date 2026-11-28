package com.cargolinklabs.loadmatch.platform.rating.domain.model.queries;

import java.util.UUID;

/**
 * Query to fetch ratings received by a profile
 */
public record GetRatingsByRateeIdQuery(UUID rateeId) {
}

