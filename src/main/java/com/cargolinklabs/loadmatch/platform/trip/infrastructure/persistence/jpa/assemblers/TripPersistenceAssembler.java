package com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.entities.TripStatusHistory;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.entities.TripPersistenceEntity;
import com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.entities.TripStatusHistoryPersistenceEntity;

/**
 * Assembler that translates between the trip aggregate and its persistence entities
 */
public final class TripPersistenceAssembler {

    private TripPersistenceAssembler() {
    }

    public static TripPersistenceEntity toPersistenceFromDomain(Trip trip) {
        var entity = new TripPersistenceEntity();
        entity.setId(trip.getId().tripId());
        entity.setLoadRequestId(trip.getLoadRequestId().loadRequestId());
        entity.setShipperId(trip.getShipperId().shipperId());
        entity.setCarrierId(trip.getCarrierId().carrierId());
        entity.setVehicleId(trip.getVehicleId().vehicleId());
        entity.setStatus(trip.getStatus());
        entity.setAssignedAt(trip.getAssignedAt());
        entity.setDeliveredAt(trip.getDeliveredAt());
        entity.setDeliveryEvidenceUrl(trip.getDeliveryEvidenceUrl());
        entity.setDeliveryObservation(trip.getDeliveryObservation());
        entity.setCompletedAt(trip.getCompletedAt());
        trip.getHistory().forEach(history -> entity.getHistory().add(toPersistenceFromDomain(history, entity)));
        return entity;
    }

    public static Trip toDomainFromPersistence(TripPersistenceEntity entity) {
        return Trip.reconstitute(
                new TripId(entity.getId()),
                new LoadRequestId(entity.getLoadRequestId()),
                new ShipperId(entity.getShipperId()),
                new CarrierId(entity.getCarrierId()),
                new VehicleId(entity.getVehicleId()),
                entity.getStatus(),
                entity.getAssignedAt(),
                entity.getDeliveredAt(),
                entity.getDeliveryEvidenceUrl(),
                entity.getDeliveryObservation(),
                entity.getCompletedAt(),
                entity.getHistory().stream().map(TripPersistenceAssembler::toDomainFromPersistence).toList());
    }

    private static TripStatusHistoryPersistenceEntity toPersistenceFromDomain(TripStatusHistory history,
                                                                              TripPersistenceEntity trip) {
        var entity = new TripStatusHistoryPersistenceEntity();
        entity.setId(history.getId());
        entity.setTrip(trip);
        entity.setPreviousStatus(history.getPreviousStatus());
        entity.setNewStatus(history.getNewStatus());
        entity.setDescription(history.getDescription());
        entity.setRegisteredAt(history.getRegisteredAt());
        return entity;
    }

    private static TripStatusHistory toDomainFromPersistence(TripStatusHistoryPersistenceEntity entity) {
        return TripStatusHistory.reconstitute(entity.getId(), entity.getPreviousStatus(), entity.getNewStatus(),
                entity.getDescription(), entity.getRegisteredAt());
    }
}
