package com.cargolinklabs.loadmatch.platform.profiles.application.queryservices;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Shipper;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetShipperByIdQuery;

import java.util.Optional;

/**
 * Shipper query service
 */
public interface ShipperQueryService {
    Optional<Shipper> handle(GetShipperByIdQuery query);
}
