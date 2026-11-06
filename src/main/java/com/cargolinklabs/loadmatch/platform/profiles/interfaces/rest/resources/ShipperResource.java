package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources;

import java.util.UUID;

/**
 * Shipper response resource
 */
public record ShipperResource(
        UUID id,
        UUID userId,
        String ruc,
        String businessName,
        String contactName,
        String phoneNumber,
        String street,
        String district,
        String province,
        String department) {
}
