package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Shipper profile update request resource
 * <p>
 *     Only the contact data and the fiscal address are editable.
 * </p>
 */
public record UpdateShipperProfileResource(
        @NotBlank(message = "{validation.not-blank}") String contactName,
        @NotBlank(message = "{validation.not-blank}") String phoneNumber,
        @NotBlank(message = "{validation.not-blank}") String street,
        @NotBlank(message = "{validation.not-blank}") String district,
        String province,
        String department) {
}
