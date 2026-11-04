package com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Sign in request resource
 *
 * @param email the email address
 * @param password the raw password
 */
public record SignInResource(
        @NotBlank(message = "{validation.not-blank}") @Email(message = "{validation.email}") String email,
        @NotBlank(message = "{validation.not-blank}") String password) {
}
