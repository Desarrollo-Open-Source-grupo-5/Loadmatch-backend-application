package com.cargolinklabs.loadmatch.platform.freight.domain.model.commands;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Command to create and publish a load request
 */
public record CreateLoadRequestCommand(
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
        LocalDateTime pickupAt) {

    public CreateLoadRequestCommand {
        if (shipperId == null) {
            throw new IllegalArgumentException("shipperId cannot be null");
        }
        if (vehicleTypeId == null) {
            throw new IllegalArgumentException("vehicleTypeId cannot be null");
        }
    }
}
