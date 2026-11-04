package com.cargolinklabs.loadmatch.platform.iam.domain.model.events;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.Roles;

import java.util.UUID;

/**
 * Domain event raised when a new user account is registered
 *
 * @param userId the identifier of the new user
 * @param email the email address of the new user
 * @param role the role of the new user
 */
public record UserRegisteredEvent(UUID userId, String email, Roles role) {
}
