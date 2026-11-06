package com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.CarrierId;

/**
 * Command to enable a carrier to accept trips
 *
 * @param carrierId the carrier identifier
 */
public record EnableCarrierCommand(CarrierId carrierId) {
}
