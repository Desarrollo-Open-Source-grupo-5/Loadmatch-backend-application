package com.cargolinklabs.loadmatch.platform.fleet.application.queryservices;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.Vehicle;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries.GetVehicleByIdQuery;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries.GetVehiclesByCarrierIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Vehicle query service
 */
public interface VehicleQueryService {
    Optional<Vehicle> handle(GetVehicleByIdQuery query);

    List<Vehicle> handle(GetVehiclesByCarrierIdQuery query);
}
