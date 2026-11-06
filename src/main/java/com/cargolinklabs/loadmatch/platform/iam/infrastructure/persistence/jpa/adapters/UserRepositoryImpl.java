package com.cargolinklabs.loadmatch.platform.iam.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.EmailAddress;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.UserId;
import com.cargolinklabs.loadmatch.platform.iam.domain.repositories.UserRepository;
import com.cargolinklabs.loadmatch.platform.iam.infrastructure.persistence.jpa.assemblers.UserPersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.iam.infrastructure.persistence.jpa.repositories.UserPersistenceRepository;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA adapter of the user repository port
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserPersistenceRepository userPersistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public UserRepositoryImpl(UserPersistenceRepository userPersistenceRepository,
                              DomainEventPublisher domainEventPublisher) {
        this.userPersistenceRepository = userPersistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return userPersistenceRepository.findById(id.userId())
                .map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {
        return userPersistenceRepository.findByEmail(email.address())
                .map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByEmail(EmailAddress email) {
        return userPersistenceRepository.existsByEmail(email.address());
    }

    @Override
    public User save(User user) {
        var saved = userPersistenceRepository.save(UserPersistenceAssembler.toPersistenceFromDomain(user));
        domainEventPublisher.publishAndClearEvents(user);
        return UserPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
