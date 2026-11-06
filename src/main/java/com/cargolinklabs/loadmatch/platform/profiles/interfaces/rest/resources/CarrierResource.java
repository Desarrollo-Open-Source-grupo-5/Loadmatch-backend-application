package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Carrier response resource
 */
public record CarrierResource(
        UUID id,
        UUID userId,
        String firstNames,
        String lastNames,
        String fullName,
        String dni,
        String licenseNumber,
        String licenseCategory,
        LocalDate licenseExpirationDate,
        String phoneNumber,
        String operatingZone,
        String photoUrl,
        String enablementStatus,
        boolean canAcceptTrips) {
}
