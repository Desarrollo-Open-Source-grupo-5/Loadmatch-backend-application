package com.cargolinklabs.loadmatch.platform.profiles.application.queryservices;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Shipper;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetShipperByIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetShipperByUserIdQuery;

import java.util.Optional;

/**
 * Shipper query service
 */
public interface ShipperQueryService {
    Optional<Shipper> handle(GetShipperByIdQuery query);

    Optional<Shipper> handle(GetShipperByUserIdQuery query);
}
