package com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Carrier;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.Dni;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.UserId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.repositories.CarrierRepository;
import com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.assemblers.CarrierPersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.repositories.CarrierPersistenceRepository;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA adapter of the carrier repository port
 */
@Repository
public class CarrierRepositoryImpl implements CarrierRepository {
    private final CarrierPersistenceRepository carrierPersistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public CarrierRepositoryImpl(CarrierPersistenceRepository carrierPersistenceRepository,
                                 DomainEventPublisher domainEventPublisher) {
        this.carrierPersistenceRepository = carrierPersistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Optional<Carrier> findById(CarrierId id) {
        return carrierPersistenceRepository.findById(id.carrierId())
                .map(CarrierPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Carrier> findByUserId(UserId userId) {
        return carrierPersistenceRepository.findByUserId(userId.userId())
                .map(CarrierPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByDni(Dni dni) {
        return carrierPersistenceRepository.existsByDni(dni.dni());
    }

    @Override
    public Carrier save(Carrier carrier) {
        var saved = carrierPersistenceRepository.save(CarrierPersistenceAssembler.toPersistenceFromDomain(carrier));
        domainEventPublisher.publishAndClearEvents(carrier);
        return CarrierPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
