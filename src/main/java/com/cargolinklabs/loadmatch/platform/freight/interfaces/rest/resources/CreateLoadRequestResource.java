package com.cargolinklabs.loadmatch.platform.freight.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Load request creation resource
 */
public record CreateLoadRequestResource(
        @NotNull(message = "{validation.not-blank}") UUID shipperId,
        @NotNull(message = "{validation.not-blank}") UUID vehicleTypeId,
        @NotBlank(message = "{validation.not-blank}") String originAddress,
        @NotBlank(message = "{validation.not-blank}") String originDistrict,
        @NotNull(message = "{validation.not-blank}") Double originLatitude,
        @NotNull(message = "{validation.not-blank}") Double originLongitude,
        @NotBlank(message = "{validation.not-blank}") String destinationAddress,
        @NotBlank(message = "{validation.not-blank}") String destinationDistrict,
        @NotNull(message = "{validation.not-blank}") Double destinationLatitude,
        @NotNull(message = "{validation.not-blank}") Double destinationLongitude,
        @NotNull(message = "{validation.not-blank}") @Positive Double weightKg,
        @NotNull(message = "{validation.not-blank}") @Positive Double lengthM,
        @NotNull(message = "{validation.not-blank}") @Positive Double widthM,
        @NotNull(message = "{validation.not-blank}") @Positive Double heightM,
        @NotBlank(message = "{validation.not-blank}") String cargoType,
        @NotNull(message = "{validation.not-blank}") @Positive BigDecimal offeredRateAmount,
        @NotNull(message = "{validation.not-blank}") LocalDateTime pickupAt) {
}
