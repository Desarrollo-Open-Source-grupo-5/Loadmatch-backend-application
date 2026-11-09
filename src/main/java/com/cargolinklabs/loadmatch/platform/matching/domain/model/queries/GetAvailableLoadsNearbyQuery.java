package com.cargolinklabs.loadmatch.platform.matching.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.matching.domain.model.valueobjects.SearchCriteria;

/**
 * Query to get the published loads whose pickup point is within the search radius
 *
 * @param criteria the search criteria
 */
public record GetAvailableLoadsNearbyQuery(SearchCriteria criteria) {
}
