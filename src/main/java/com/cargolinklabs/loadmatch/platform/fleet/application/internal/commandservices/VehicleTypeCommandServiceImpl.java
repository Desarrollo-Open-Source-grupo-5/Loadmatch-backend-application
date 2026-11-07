package com.cargolinklabs.loadmatch.platform.fleet.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.fleet.application.commandservices.VehicleTypeCommandService;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.VehicleType;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.SeedVehicleTypesCommand;
import com.cargolinklabs.loadmatch.platform.fleet.domain.repositories.VehicleTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Vehicle type command service implementation
 */
@Service
public class VehicleTypeCommandServiceImpl implements VehicleTypeCommandService {
    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleTypeCommandServiceImpl(VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @Override
    public void handle(SeedVehicleTypesCommand command) {
        List.of(
                new VehicleType("Camioneta", "Camioneta para cargas ligeras", 1_000),
                new VehicleType("Furgón", "Furgón cerrado para cargas pequeñas y medianas", 3_500),
                new VehicleType("Camión de 2 ejes", "Camión rígido de dos ejes", 10_000),
                new VehicleType("Camión de 3 ejes", "Camión rígido de tres ejes", 17_000),
                new VehicleType("Tracto con semirremolque", "Tracto camión con semirremolque", 30_000)
        ).forEach(vehicleType -> {
            if (!vehicleTypeRepository.existsByName(vehicleType.getName())) {
                vehicleTypeRepository.save(vehicleType);
            }
        });
    }
}
