package com.cargolinklabs.loadmatch.platform.rating.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.aggregates.Rating;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RatingId;
import com.cargolinklabs.loadmatch.platform.rating.domain.repositories.RatingRepository;
import com.cargolinklabs.loadmatch.platform.rating.infrastructure.persistence.jpa.assemblers.RatingPersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.rating.infrastructure.persistence.jpa.repositories.RatingPersistenceRepository;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.events.DomainEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA adapter implementing RatingRepository
 */
@Repository
public class RatingRepositoryImpl implements RatingRepository {
    private final RatingPersistenceRepository ratingPersistenceRepository;
    private final DomainEventPublisher domainEventPublisher;

    public RatingRepositoryImpl(RatingPersistenceRepository ratingPersistenceRepository,
                                DomainEventPublisher domainEventPublisher) {
        this.ratingPersistenceRepository = ratingPersistenceRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public Rating save(Rating rating) {
        var entity = RatingPersistenceAssembler.toPersistenceFromDomain(rating);
        var saved = ratingPersistenceRepository.save(entity);
        domainEventPublisher.publishAndClearEvents(rating);
        return RatingPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<Rating> findById(RatingId id) {
        return ratingPersistenceRepository.findById(id.ratingId())
                .map(RatingPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByTripIdAndRaterId(UUID tripId, UUID raterId) {
        return ratingPersistenceRepository.existsByTripIdAndRaterId(tripId, raterId);
    }

    @Override
    public List<Rating> findAllByRateeId(UUID rateeId) {
        return ratingPersistenceRepository.findAllByRateeId(rateeId).stream()
                .map(RatingPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}

