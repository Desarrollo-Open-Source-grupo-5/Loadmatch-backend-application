package com.cargolinklabs.loadmatch.platform.fleet.domain.repositories;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.VehicleType;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleTypeId;

import java.util.List;
import java.util.Optional;

/**
 * Vehicle type repository port
 */
public interface VehicleTypeRepository {
    Optional<VehicleType> findById(VehicleTypeId id);

    List<VehicleType> findAllActive();

    boolean existsByName(String name);

    VehicleType save(VehicleType vehicleType);
}
