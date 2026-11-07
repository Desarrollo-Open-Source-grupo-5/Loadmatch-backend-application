package com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.VehicleType;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleTypeId;
import com.cargolinklabs.loadmatch.platform.fleet.domain.repositories.VehicleTypeRepository;
import com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.assemblers.VehicleTypePersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.repositories.VehicleTypePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA adapter of the vehicle type repository port
 */
@Repository
public class VehicleTypeRepositoryImpl implements VehicleTypeRepository {
    private final VehicleTypePersistenceRepository vehicleTypePersistenceRepository;

    public VehicleTypeRepositoryImpl(VehicleTypePersistenceRepository vehicleTypePersistenceRepository) {
        this.vehicleTypePersistenceRepository = vehicleTypePersistenceRepository;
    }

    @Override
    public Optional<VehicleType> findById(VehicleTypeId id) {
        return vehicleTypePersistenceRepository.findById(id.vehicleTypeId())
                .map(VehicleTypePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<VehicleType> findAllActive() {
        return vehicleTypePersistenceRepository.findByActiveTrueOrderByMaxWeightKgAsc().stream()
                .map(VehicleTypePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return vehicleTypePersistenceRepository.existsByName(name);
    }

    @Override
    public VehicleType save(VehicleType vehicleType) {
        var saved = vehicleTypePersistenceRepository.save(
                VehicleTypePersistenceAssembler.toPersistenceFromDomain(vehicleType));
        return VehicleTypePersistenceAssembler.toDomainFromPersistence(saved);
    }
}
