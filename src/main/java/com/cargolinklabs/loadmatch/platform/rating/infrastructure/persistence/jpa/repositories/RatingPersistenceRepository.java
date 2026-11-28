package com.cargolinklabs.loadmatch.platform.rating.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.rating.infrastructure.persistence.jpa.entities.RatingPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for RatingPersistenceEntity
 */
public interface RatingPersistenceRepository extends JpaRepository<RatingPersistenceEntity, UUID> {
    boolean existsByTripIdAndRaterId(UUID tripId, UUID raterId);

    List<RatingPersistenceEntity> findAllByRateeId(UUID rateeId);
}

