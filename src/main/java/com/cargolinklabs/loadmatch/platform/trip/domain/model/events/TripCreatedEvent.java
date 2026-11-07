package com.cargolinklabs.loadmatch.platform.trip.domain.model.events;

import java.util.UUID;

/**
 * Domain event raised when a carrier accepts a load request and the trip is created
 *
 * @param tripId the trip identifier
 * @param loadRequestId the load request identifier
 * @param shipperId the shipper to notify
 * @param carrierId the carrier that accepted the load
 */
public record TripCreatedEvent(UUID tripId, UUID loadRequestId, UUID shipperId, UUID carrierId) {
}
