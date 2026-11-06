package com.cargolinklabs.loadmatch.platform.iam.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.iam.application.queryservices.UserQueryService;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.cargolinklabs.loadmatch.platform.iam.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User query service implementation
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.email());
    }
}
