package com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.UserId;

/**
 * Query to get the shipper that belongs to an IAM user
 *
 * @param userId the user identifier
 */
public record GetShipperByUserIdQuery(UserId userId) {
}
