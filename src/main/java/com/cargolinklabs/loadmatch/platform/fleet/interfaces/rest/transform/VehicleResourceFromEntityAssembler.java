package com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.Vehicle;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources.VehicleResource;

/**
 * Assembler that translates a vehicle into its response resource
 */
public final class VehicleResourceFromEntityAssembler {

    private VehicleResourceFromEntityAssembler() {
    }

    public static VehicleResource toResourceFromEntity(Vehicle vehicle) {
        return new VehicleResource(
                vehicle.getId().vehicleId(),
                vehicle.getCarrierId().carrierId(),
                vehicle.getVehicleTypeId().vehicleTypeId(),
                vehicle.getLicensePlate().licensePlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getManufactureYear(),
                vehicle.getPayloadKg(),
                vehicle.getMtcValidationStatus().name(),
                vehicle.isActive());
    }
}
