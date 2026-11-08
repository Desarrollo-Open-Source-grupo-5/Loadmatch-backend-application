package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Trip status update resource sent by the carrier
 *
 * @param status the new status (EN_ROUTE_TO_PICKUP, AT_PICKUP_POINT, CARGO_PICKED_UP, IN_TRANSIT, DELAYED or DELIVERED)
 * @param description an optional description of the update
 * @param evidenceUrl the URL of the delivery evidence, required for DELIVERED
 */
public record UpdateTripStatusResource(
        @NotBlank(message = "{validation.not-blank}") String status,
        String description,
        String evidenceUrl) {
}
