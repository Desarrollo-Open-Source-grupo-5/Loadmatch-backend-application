package com.cargolinklabs.loadmatch.platform.freight.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestId;

/**
 * Query to get a load request by its identifier
 *
 * @param loadRequestId the load request identifier
 */
public record GetLoadRequestByIdQuery(LoadRequestId loadRequestId) {
}
