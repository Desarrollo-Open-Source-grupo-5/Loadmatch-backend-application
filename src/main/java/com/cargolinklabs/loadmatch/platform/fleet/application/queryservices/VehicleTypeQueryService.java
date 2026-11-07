package com.cargolinklabs.loadmatch.platform.fleet.application.queryservices;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.VehicleType;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries.GetAllVehicleTypesQuery;

import java.util.List;

/**
 * Vehicle type query service
 */
public interface VehicleTypeQueryService {
    List<VehicleType> handle(GetAllVehicleTypesQuery query);
}
