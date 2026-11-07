package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Carrier profile update request resource
 */
public record UpdateCarrierProfileResource(
        @NotBlank(message = "{validation.not-blank}") String phoneNumber,
        String operatingZone,
        String photoUrl) {
}
