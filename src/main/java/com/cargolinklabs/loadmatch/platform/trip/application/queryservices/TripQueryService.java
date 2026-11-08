package com.cargolinklabs.loadmatch.platform.trip.application.queryservices;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripByIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripsByCarrierIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripsByShipperIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Trip query service
 */
public interface TripQueryService {
    Optional<Trip> handle(GetTripByIdQuery query);

    List<Trip> handle(GetTripsByCarrierIdQuery query);

    List<Trip> handle(GetTripsByShipperIdQuery query);
}
