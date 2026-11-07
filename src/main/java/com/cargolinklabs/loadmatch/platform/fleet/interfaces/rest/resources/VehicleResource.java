package com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources;

import java.util.UUID;

/**
 * Vehicle response resource
 */
public record VehicleResource(
        UUID id,
        UUID carrierId,
        UUID vehicleTypeId,
        String licensePlate,
        String brand,
        String model,
        int manufactureYear,
        double payloadKg,
        String mtcValidationStatus,
        boolean active) {
}
