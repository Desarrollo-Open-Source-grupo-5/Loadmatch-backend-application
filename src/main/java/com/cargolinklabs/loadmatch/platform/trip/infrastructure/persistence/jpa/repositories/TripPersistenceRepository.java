package com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripStatus;
import com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.entities.TripPersistenceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for trip persistence entities
 */
@Repository
public interface TripPersistenceRepository extends JpaRepository<TripPersistenceEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select trip from TripPersistenceEntity trip where trip.id = :id")
    Optional<TripPersistenceEntity> findByIdForUpdate(@Param("id") UUID id);

    List<TripPersistenceEntity> findByCarrierIdOrderByAssignedAtDesc(UUID carrierId);

    List<TripPersistenceEntity> findByShipperIdOrderByAssignedAtDesc(UUID shipperId);

    List<TripPersistenceEntity> findByStatusAndDeliveredAtBefore(TripStatus status, LocalDateTime deliveredAt);
}
