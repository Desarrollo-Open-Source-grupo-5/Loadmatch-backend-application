package com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.resources;

import java.util.UUID;

/**
 * User response resource
 *
 * @param id the user identifier
 * @param email the email address
 * @param role the role name
 * @param active whether the account is active
 */
public record UserResource(UUID id, String email, String role, boolean active) {
}
