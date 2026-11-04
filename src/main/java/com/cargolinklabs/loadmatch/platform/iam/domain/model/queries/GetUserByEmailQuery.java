package com.cargolinklabs.loadmatch.platform.iam.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.EmailAddress;

/**
 * Query to get a user by its email address
 *
 * @param email the email address
 */
public record GetUserByEmailQuery(EmailAddress email) {
}
