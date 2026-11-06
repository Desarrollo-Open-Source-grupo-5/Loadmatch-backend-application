package com.cargolinklabs.loadmatch.platform.iam.application.queryservices;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.queries.GetUserByIdQuery;

import java.util.Optional;

/**
 * User query service
 */
public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);

    Optional<User> handle(GetUserByEmailQuery query);
}
