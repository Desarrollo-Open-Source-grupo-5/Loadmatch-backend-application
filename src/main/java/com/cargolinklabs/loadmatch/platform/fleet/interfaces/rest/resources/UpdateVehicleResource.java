package com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

/**
 * Vehicle update request resource; the license plate is not editable
 */
public record UpdateVehicleResource(
        @NotNull(message = "{validation.not-blank}") UUID vehicleTypeId,
        @NotBlank(message = "{validation.not-blank}") String brand,
        @NotBlank(message = "{validation.not-blank}") String model,
        @NotNull(message = "{validation.not-blank}") Integer manufactureYear,
        @NotNull(message = "{validation.not-blank}") @Positive Double payloadKg) {
}
