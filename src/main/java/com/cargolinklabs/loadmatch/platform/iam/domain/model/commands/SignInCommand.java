package com.cargolinklabs.loadmatch.platform.iam.domain.model.commands;

/**
 * Command to authenticate a user
 *
 * @param email the email address
 * @param password the raw password
 */
public record SignInCommand(String email, String password) {
    public SignInCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password cannot be null or blank");
        }
    }
}
