package com.cargolinklabs.loadmatch.platform.matching.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Available load response resource
 */
public record AvailableLoadResource(
        UUID loadRequestId,
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
        String cargoType,
        BigDecimal offeredRateAmount,
        String currency,
        LocalDateTime pickupAt,
        boolean urgent,
        double distanceToCarrierKm,
        double routeDistanceKm) {
}
