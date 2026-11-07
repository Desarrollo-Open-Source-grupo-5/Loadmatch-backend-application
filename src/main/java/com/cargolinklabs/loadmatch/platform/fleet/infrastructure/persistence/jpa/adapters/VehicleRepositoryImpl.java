package com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.Vehicle;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.LicensePlate;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleId;
import com.cargolinklabs.loadmatch.platform.fleet.domain.repositories.VehicleRepository;
import com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.assemblers.VehiclePersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.repositories.VehiclePersistenceRepository;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA adapter of the vehicle repository port
 */
@Repository
public class VehicleRepositoryImpl implements VehicleRepository {
    private final VehiclePersistenceRepository vehiclePersistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public VehicleRepositoryImpl(VehiclePersistenceRepository vehiclePersistenceRepository,
                                 DomainEventPublisher domainEventPublisher) {
        this.vehiclePersistenceRepository = vehiclePersistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Optional<Vehicle> findById(VehicleId id) {
        return vehiclePersistenceRepository.findById(id.vehicleId())
                .map(VehiclePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Vehicle> findByCarrierId(CarrierId carrierId) {
        return vehiclePersistenceRepository.findByCarrierIdOrderByCreatedAtAsc(carrierId.carrierId()).stream()
                .map(VehiclePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsByLicensePlate(LicensePlate licensePlate) {
        return vehiclePersistenceRepository.existsByLicensePlate(licensePlate.licensePlate());
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        var saved = vehiclePersistenceRepository.save(VehiclePersistenceAssembler.toPersistenceFromDomain(vehicle));
        domainEventPublisher.publishAndClearEvents(vehicle);
        return VehiclePersistenceAssembler.toDomainFromPersistence(saved);
    }
}
