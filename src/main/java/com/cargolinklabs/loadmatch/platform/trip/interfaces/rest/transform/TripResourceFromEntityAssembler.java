package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.TripResource;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.TripStatusHistoryResource;

/**
 * Assembler that translates a trip into its response resource
 */
public final class TripResourceFromEntityAssembler {

    private TripResourceFromEntityAssembler() {
    }

    public static TripResource toResourceFromEntity(Trip trip) {
        return new TripResource(
                trip.getId().tripId(),
                trip.getLoadRequestId().loadRequestId(),
                trip.getShipperId().shipperId(),
                trip.getCarrierId().carrierId(),
                trip.getVehicleId().vehicleId(),
                trip.getStatus().name(),
                trip.getAssignedAt(),
                trip.getDeliveredAt(),
                trip.getDeliveryEvidenceUrl(),
                trip.getDeliveryObservation(),
                trip.getCompletedAt(),
                trip.getHistory().stream()
                        .map(history -> new TripStatusHistoryResource(
                                history.getPreviousStatus() != null ? history.getPreviousStatus().name() : null,
                                history.getNewStatus().name(),
                                history.getDescription(),
                                history.getRegisteredAt()))
                        .toList());
    }
}
