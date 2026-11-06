package com.cargolinklabs.loadmatch.platform.profiles.domain.model.events;

import java.util.UUID;

/**
 * Domain event raised when a shipper completes its registration
 *
 * @param shipperId the shipper identifier
 * @param userId the identifier of the IAM user
 * @param ruc the RUC of the business
 */
public record ShipperRegisteredEvent(UUID shipperId, UUID userId, String ruc) {
}
