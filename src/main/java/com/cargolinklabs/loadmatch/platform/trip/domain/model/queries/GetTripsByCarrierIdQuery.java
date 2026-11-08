package com.cargolinklabs.loadmatch.platform.trip.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.CarrierId;

/**
 * Query to get the trips of a carrier
 *
 * @param carrierId the carrier identifier
 */
public record GetTripsByCarrierIdQuery(CarrierId carrierId) {
}
