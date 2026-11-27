package com.cargolinklabs.loadmatch.platform.trip.interfaces.acl;

import java.util.UUID;

/**
 * Snapshot of a trip shared with other bounded contexts through the facade
 *
 * @param tripId the trip identifier
 * @param loadRequestId the load request the trip originates from
 * @param shipperId the shipper that hired the trip
 * @param carrierId the carrier that executes the trip
 * @param status the current trip status name
 */
public record TripSnapshot(UUID tripId, UUID loadRequestId, UUID shipperId, UUID carrierId, String status) {
}
