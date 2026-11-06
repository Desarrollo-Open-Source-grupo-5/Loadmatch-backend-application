package com.cargolinklabs.loadmatch.platform.freight.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.ShipperId;

/**
 * Query to get the load requests published by a shipper
 *
 * @param shipperId the shipper identifier
 */
public record GetLoadRequestsByShipperIdQuery(ShipperId shipperId) {
}
