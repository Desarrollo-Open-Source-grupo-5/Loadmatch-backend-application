package com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.ShipperId;

/**
 * Query to get a shipper by its identifier
 *
 * @param shipperId the shipper identifier
 */
public record GetShipperByIdQuery(ShipperId shipperId) {
}
