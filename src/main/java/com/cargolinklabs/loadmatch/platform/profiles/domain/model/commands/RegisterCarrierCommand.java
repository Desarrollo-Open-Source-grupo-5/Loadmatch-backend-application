package com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands;

import java.time.LocalDate;

/**
 * Command to register a carrier together with its user account
 */
public record RegisterCarrierCommand(
        String email,
        String password,
        String firstNames,
        String lastNames,
        String dni,
        String licenseNumber,
        String licenseCategory,
        LocalDate licenseExpirationDate,
        String phoneNumber,
        String operatingZone) {

    public RegisterCarrierCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password cannot be null or blank");
        }
        if (dni == null || dni.isBlank()) {
            throw new IllegalArgumentException("dni cannot be null or blank");
        }
    }
}
