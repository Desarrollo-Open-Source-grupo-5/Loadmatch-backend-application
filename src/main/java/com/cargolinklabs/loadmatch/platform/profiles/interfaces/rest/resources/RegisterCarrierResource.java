package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Carrier registration request resource
 */
public record RegisterCarrierResource(
        @NotBlank(message = "{validation.not-blank}") @Email(message = "{validation.email}") String email,
        @NotBlank(message = "{validation.not-blank}") String password,
        @NotBlank(message = "{validation.not-blank}") String firstNames,
        @NotBlank(message = "{validation.not-blank}") String lastNames,
        @NotBlank(message = "{validation.not-blank}") String dni,
        @NotBlank(message = "{validation.not-blank}") String licenseNumber,
        @NotBlank(message = "{validation.not-blank}") String licenseCategory,
        @NotNull(message = "{validation.not-blank}") LocalDate licenseExpirationDate,
        @NotBlank(message = "{validation.not-blank}") String phoneNumber,
        String operatingZone) {
}
