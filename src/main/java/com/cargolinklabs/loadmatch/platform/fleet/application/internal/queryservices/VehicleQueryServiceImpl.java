package com.cargolinklabs.loadmatch.platform.fleet.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.fleet.application.queryservices.VehicleQueryService;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.Vehicle;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries.GetVehicleByIdQuery;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries.GetVehiclesByCarrierIdQuery;
import com.cargolinklabs.loadmatch.platform.fleet.domain.repositories.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Vehicle query service implementation
 */
@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {
    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.vehicleId());
    }

    @Override
    public List<Vehicle> handle(GetVehiclesByCarrierIdQuery query) {
        return vehicleRepository.findByCarrierId(query.carrierId());
    }
}
