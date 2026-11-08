package com.cargolinklabs.loadmatch.platform.matching.application.queryservices;

import com.cargolinklabs.loadmatch.platform.matching.domain.model.queries.GetAvailableLoadsNearbyQuery;
import com.cargolinklabs.loadmatch.platform.matching.domain.model.readmodels.AvailableLoad;

import java.util.List;

/**
 * Available load query service
 */
public interface AvailableLoadQueryService {
    /**
     * Finds the published loads near the carrier, sorted by distance to the pickup point
     * @param query the nearby loads query
     * @return the available loads within the radius
     */
    List<AvailableLoad> handle(GetAvailableLoadsNearbyQuery query);
}
