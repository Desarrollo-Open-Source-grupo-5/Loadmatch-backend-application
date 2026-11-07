package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Load request acceptance resource sent by a carrier
 */
public record AcceptLoadRequestResource(
        @NotNull(message = "{validation.not-blank}") UUID loadRequestId,
        @NotNull(message = "{validation.not-blank}") UUID carrierId,
        @NotNull(message = "{validation.not-blank}") UUID vehicleId) {
}
