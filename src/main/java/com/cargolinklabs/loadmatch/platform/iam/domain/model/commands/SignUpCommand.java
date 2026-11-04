package com.cargolinklabs.loadmatch.platform.iam.domain.model.commands;

/**
 * Command to register a new user account
 *
 * @param email the email address
 * @param password the raw password
 * @param role the role name (SHIPPER or CARRIER)
 */
public record SignUpCommand(String email, String password, String role) {
    public SignUpCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email cannot be null or blank");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("password must have at least 8 characters");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("role cannot be null or blank");
        }
    }
}
