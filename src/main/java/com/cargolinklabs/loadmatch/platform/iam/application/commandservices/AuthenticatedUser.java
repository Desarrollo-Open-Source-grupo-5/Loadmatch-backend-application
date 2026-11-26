package com.cargolinklabs.loadmatch.platform.iam.application.commandservices;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;

/**
 * Result of a successful sign in: the authenticated user and its bearer token
 *
 * @param user the authenticated user
 * @param token the signed bearer token
 */
public record AuthenticatedUser(User user, String token) {
}
