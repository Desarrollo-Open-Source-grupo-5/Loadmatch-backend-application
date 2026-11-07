package com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.CarrierId;

/**
 * Query to get the vehicles of a carrier
 *
 * @param carrierId the carrier identifier
 */
public record GetVehiclesByCarrierIdQuery(CarrierId carrierId) {
}
