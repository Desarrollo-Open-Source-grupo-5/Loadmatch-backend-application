package com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripStatus;
import com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.entities.TripPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for trip persistence entities
 */
@Repository
public interface TripPersistenceRepository extends JpaRepository<TripPersistenceEntity, UUID> {
    List<TripPersistenceEntity> findByCarrierIdOrderByAssignedAtDesc(UUID carrierId);

    List<TripPersistenceEntity> findByShipperIdOrderByAssignedAtDesc(UUID shipperId);

    List<TripPersistenceEntity> findByStatusAndDeliveredAtBefore(TripStatus status, LocalDateTime deliveredAt);
}
