package com.cargolinklabs.loadmatch.platform.freight.interfaces.acl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Snapshot of a published load request shared with other bounded contexts through the facade
 */
public record PublishedLoadRequestSnapshot(
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
        boolean urgent) {
}
