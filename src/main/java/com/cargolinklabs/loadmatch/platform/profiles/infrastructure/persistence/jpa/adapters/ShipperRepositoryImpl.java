package com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Shipper;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.Ruc;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.UserId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.repositories.ShipperRepository;
import com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.assemblers.ShipperPersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.repositories.ShipperPersistenceRepository;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA adapter of the shipper repository port
 */
@Repository
public class ShipperRepositoryImpl implements ShipperRepository {
    private final ShipperPersistenceRepository shipperPersistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public ShipperRepositoryImpl(ShipperPersistenceRepository shipperPersistenceRepository,
                                 DomainEventPublisher domainEventPublisher) {
        this.shipperPersistenceRepository = shipperPersistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Optional<Shipper> findById(ShipperId id) {
        return shipperPersistenceRepository.findById(id.shipperId())
                .map(ShipperPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Shipper> findByUserId(UserId userId) {
        return shipperPersistenceRepository.findByUserId(userId.userId())
                .map(ShipperPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByRuc(Ruc ruc) {
        return shipperPersistenceRepository.existsByRuc(ruc.ruc());
    }

    @Override
    public Shipper save(Shipper shipper) {
        var saved = shipperPersistenceRepository.save(ShipperPersistenceAssembler.toPersistenceFromDomain(shipper));
        domainEventPublisher.publishAndClearEvents(shipper);
        return ShipperPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
