package com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.resources;

import java.util.UUID;

/**
 * Authenticated user response resource
 *
 * @param id the user identifier
 * @param email the email address
 * @param role the role name
 */
public record AuthenticatedUserResource(UUID id, String email, String role) {
}
