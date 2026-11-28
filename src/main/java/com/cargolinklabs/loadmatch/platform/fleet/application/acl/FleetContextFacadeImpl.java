package com.cargolinklabs.loadmatch.platform.fleet.application.acl;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.Vehicle;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.LicensePlate;
import com.cargolinklabs.loadmatch.platform.fleet.domain.repositories.VehicleRepository;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.acl.FleetContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FleetContextFacadeImpl implements FleetContextFacade {
    private final VehicleRepository vehicleRepository;

    public FleetContextFacadeImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<String> fetchActivePlateByCarrierId(UUID carrierId) {
        return vehicleRepository.findByCarrierId(new CarrierId(carrierId)).stream()
                .filter(Vehicle::isActive)
                .map(v -> v.getLicensePlate().licensePlate())
                .findFirst();
    }

    @Override
    public boolean hasActivePlate(UUID carrierId, String plate) {
        LicensePlate normalized;
        try {
            normalized = new LicensePlate(plate);
        } catch (IllegalArgumentException exception) {
            return false;
        }
        return vehicleRepository.findByCarrierId(new CarrierId(carrierId)).stream()
                .filter(Vehicle::isActive)
                .anyMatch(vehicle -> normalized.equals(vehicle.getLicensePlate()));
    }
}
