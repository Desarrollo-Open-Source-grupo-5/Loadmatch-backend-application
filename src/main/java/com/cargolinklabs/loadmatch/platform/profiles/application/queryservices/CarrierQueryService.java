package com.cargolinklabs.loadmatch.platform.profiles.application.queryservices;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Carrier;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetCarrierByIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetCarrierByUserIdQuery;

import java.util.Optional;

/**
 * Carrier query service
 */
public interface CarrierQueryService {
    Optional<Carrier> handle(GetCarrierByIdQuery query);

    Optional<Carrier> handle(GetCarrierByUserIdQuery query);
}
