package com.cargolinklabs.loadmatch.platform.iam.domain.repositories;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.EmailAddress;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.UserId;

import java.util.Optional;

/**
 * User repository port
 */
public interface UserRepository {
    Optional<User> findById(UserId id);

    Optional<User> findByEmail(EmailAddress email);

    boolean existsByEmail(EmailAddress email);

    User save(User user);
}
