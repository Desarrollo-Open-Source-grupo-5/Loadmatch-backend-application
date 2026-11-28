package com.cargolinklabs.loadmatch.platform.trip.domain.model.commands;

import java.util.UUID;

/**
 * Command used by a carrier to accept a load request, which creates the trip
 *
 * @param loadRequestId the load request to accept
 * @param carrierId the carrier that accepts it
 * @param vehicleId the vehicle the carrier will use
 * @param carrierEmail the authenticated carrier account
 */
public record AcceptLoadRequestCommand(UUID loadRequestId, UUID carrierId, UUID vehicleId, String carrierEmail) {
    public AcceptLoadRequestCommand {
        if (loadRequestId == null || carrierId == null || vehicleId == null) {
            throw new IllegalArgumentException("loadRequestId, carrierId and vehicleId cannot be null");
        }
        if (carrierEmail == null || carrierEmail.isBlank()) {
            throw new IllegalArgumentException("The authenticated carrier email is required");
        }
    }
}
