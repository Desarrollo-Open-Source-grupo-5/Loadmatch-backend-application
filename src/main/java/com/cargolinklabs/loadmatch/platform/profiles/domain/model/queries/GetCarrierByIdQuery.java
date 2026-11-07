package com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.CarrierId;

/**
 * Query to get a carrier by its identifier
 *
 * @param carrierId the carrier identifier
 */
public record GetCarrierByIdQuery(CarrierId carrierId) {
}
