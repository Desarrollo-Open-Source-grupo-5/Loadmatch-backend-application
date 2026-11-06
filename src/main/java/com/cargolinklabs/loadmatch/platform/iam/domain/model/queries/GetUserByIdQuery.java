package com.cargolinklabs.loadmatch.platform.iam.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.UserId;

/**
 * Query to get a user by its identifier
 *
 * @param userId the user identifier
 */
public record GetUserByIdQuery(UserId userId) {
}
