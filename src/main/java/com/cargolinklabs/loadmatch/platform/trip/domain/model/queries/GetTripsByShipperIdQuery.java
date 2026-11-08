package com.cargolinklabs.loadmatch.platform.trip.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.ShipperId;

/**
 * Query to get the trips of the loads of a shipper
 *
 * @param shipperId the shipper identifier
 */
public record GetTripsByShipperIdQuery(ShipperId shipperId) {
}
