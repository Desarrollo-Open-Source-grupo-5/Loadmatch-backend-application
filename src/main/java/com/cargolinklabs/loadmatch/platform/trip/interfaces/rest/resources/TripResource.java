package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Trip response resource
 */
public record TripResource(
        UUID id,
        UUID loadRequestId,
        UUID shipperId,
        UUID carrierId,
        UUID vehicleId,
        String status,
        LocalDateTime assignedAt,
        LocalDateTime deliveredAt,
        String deliveryEvidenceUrl,
        String deliveryObservation,
        LocalDateTime completedAt,
        List<TripStatusHistoryResource> history) {
}
