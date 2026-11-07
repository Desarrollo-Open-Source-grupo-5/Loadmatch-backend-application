package com.cargolinklabs.loadmatch.platform.fleet.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.fleet.application.queryservices.VehicleTypeQueryService;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.VehicleType;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries.GetAllVehicleTypesQuery;
import com.cargolinklabs.loadmatch.platform.fleet.domain.repositories.VehicleTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Vehicle type query service implementation
 */
@Service
public class VehicleTypeQueryServiceImpl implements VehicleTypeQueryService {
    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleTypeQueryServiceImpl(VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    @Override
    public List<VehicleType> handle(GetAllVehicleTypesQuery query) {
        return vehicleTypeRepository.findAllActive();
    }
}
