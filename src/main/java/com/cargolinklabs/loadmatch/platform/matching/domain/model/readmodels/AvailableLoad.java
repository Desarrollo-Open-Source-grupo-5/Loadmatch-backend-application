package com.cargolinklabs.loadmatch.platform.matching.domain.model.readmodels;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Read model of a published load that is near a carrier
 */
public record AvailableLoad(
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
