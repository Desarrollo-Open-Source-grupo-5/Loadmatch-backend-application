package com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands;

/**
 * Command to register a shipper together with its user account
 */
public record RegisterShipperCommand(
        String email,
        String password,
        String ruc,
        String businessName,
        String contactName,
        String phoneNumber,
        String street,
        String district,
        String province,
        String department) {

    public RegisterShipperCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password cannot be null or blank");
        }
        if (ruc == null || ruc.isBlank()) {
            throw new IllegalArgumentException("ruc cannot be null or blank");
        }
        if (businessName == null || businessName.isBlank()) {
            throw new IllegalArgumentException("businessName cannot be null or blank");
        }
    }
}
