package com.cargolinklabs.loadmatch.platform.profiles.application.queryservices;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Carrier;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetCarrierByIdQuery;

import java.util.Optional;

/**
 * Carrier query service
 */
public interface CarrierQueryService {
    Optional<Carrier> handle(GetCarrierByIdQuery query);
}
