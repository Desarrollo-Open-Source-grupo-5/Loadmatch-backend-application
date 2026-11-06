package com.cargolinklabs.loadmatch.platform.freight.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Load request response resource
 */
public record LoadRequestResource(
        UUID id,
        UUID shipperId,
        UUID vehicleTypeId,
        String originAddress,
        String originDistrict,
        double originLatitude,
        double originLongitude,
        String destinationAddress,
        String destinationDistrict,
        double destinationLatitude,
        double destinationLongitude,
        double weightKg,
        double lengthM,
        double widthM,
        double heightM,
        String cargoType,
        BigDecimal offeredRateAmount,
        String currency,
        String status,
        LocalDateTime pickupAt,
        LocalDateTime publishedAt,
        boolean urgent) {
}
