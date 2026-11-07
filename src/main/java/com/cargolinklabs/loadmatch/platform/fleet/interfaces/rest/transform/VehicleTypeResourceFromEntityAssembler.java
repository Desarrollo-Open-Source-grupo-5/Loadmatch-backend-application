package com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.VehicleType;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources.VehicleTypeResource;

/**
 * Assembler that translates a vehicle type into its response resource
 */
public final class VehicleTypeResourceFromEntityAssembler {

    private VehicleTypeResourceFromEntityAssembler() {
    }

    public static VehicleTypeResource toResourceFromEntity(VehicleType vehicleType) {
        return new VehicleTypeResource(vehicleType.getId().vehicleTypeId(), vehicleType.getName(),
                vehicleType.getDescription(), vehicleType.getMaxWeightKg());
    }
}
