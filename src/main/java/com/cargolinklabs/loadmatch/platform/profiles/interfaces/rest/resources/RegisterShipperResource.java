package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Shipper registration request resource
 */
public record RegisterShipperResource(
        @NotBlank(message = "{validation.not-blank}") @Email(message = "{validation.email}") String email,
        @NotBlank(message = "{validation.not-blank}") String password,
        @NotBlank(message = "{validation.not-blank}") String ruc,
        @NotBlank(message = "{validation.not-blank}") String businessName,
        @NotBlank(message = "{validation.not-blank}") String contactName,
        @NotBlank(message = "{validation.not-blank}") String phoneNumber,
        @NotBlank(message = "{validation.not-blank}") String street,
        @NotBlank(message = "{validation.not-blank}") String district,
        String province,
        String department) {
}
