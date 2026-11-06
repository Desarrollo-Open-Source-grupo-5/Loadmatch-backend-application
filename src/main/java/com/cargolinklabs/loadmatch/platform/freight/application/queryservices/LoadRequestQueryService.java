package com.cargolinklabs.loadmatch.platform.freight.application.queryservices;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.aggregates.LoadRequest;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.queries.GetLoadRequestByIdQuery;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.queries.GetLoadRequestsByShipperIdQuery;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.queries.GetPublishedLoadRequestsQuery;

import java.util.List;
import java.util.Optional;

/**
 * Load request query service
 */
public interface LoadRequestQueryService {
    Optional<LoadRequest> handle(GetLoadRequestByIdQuery query);

    List<LoadRequest> handle(GetLoadRequestsByShipperIdQuery query);

    List<LoadRequest> handle(GetPublishedLoadRequestsQuery query);
}
