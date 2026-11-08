package com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.shared.infrastructure.events.DomainEventPublisher;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripStatus;
import com.cargolinklabs.loadmatch.platform.trip.domain.repositories.TripRepository;
import com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.assemblers.TripPersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.repositories.TripPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JPA adapter of the trip repository port
 */
@Repository
public class TripRepositoryImpl implements TripRepository {
    private final TripPersistenceRepository tripPersistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public TripRepositoryImpl(TripPersistenceRepository tripPersistenceRepository,
                              DomainEventPublisher domainEventPublisher) {
        this.tripPersistenceRepository = tripPersistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Optional<Trip> findById(TripId id) {
        return tripPersistenceRepository.findById(id.tripId())
                .map(TripPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Trip> findByCarrierId(CarrierId carrierId) {
        return tripPersistenceRepository.findByCarrierIdOrderByAssignedAtDesc(carrierId.carrierId()).stream()
                .map(TripPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Trip> findByShipperId(ShipperId shipperId) {
        return tripPersistenceRepository.findByShipperIdOrderByAssignedAtDesc(shipperId.shipperId()).stream()
                .map(TripPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Trip> findDeliveredBefore(LocalDateTime deliveredAt) {
        return tripPersistenceRepository.findByStatusAndDeliveredAtBefore(TripStatus.DELIVERED, deliveredAt).stream()
                .map(TripPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Trip save(Trip trip) {
        var saved = tripPersistenceRepository.save(TripPersistenceAssembler.toPersistenceFromDomain(trip));
        domainEventPublisher.publishAndClearEvents(trip);
        return TripPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
