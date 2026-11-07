package com.cargolinklabs.loadmatch.platform.fleet.domain.repositories;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.Vehicle;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.LicensePlate;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleId;

import java.util.List;
import java.util.Optional;

/**
 * Vehicle repository port
 */
public interface VehicleRepository {
    Optional<Vehicle> findById(VehicleId id);

    List<Vehicle> findByCarrierId(CarrierId carrierId);

    boolean existsByLicensePlate(LicensePlate licensePlate);

    Vehicle save(Vehicle vehicle);
}
