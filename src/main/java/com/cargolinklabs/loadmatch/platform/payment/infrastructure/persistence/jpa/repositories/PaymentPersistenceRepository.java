package com.cargolinklabs.loadmatch.platform.payment.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.payment.infrastructure.persistence.jpa.entities.PaymentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for payment persistence entities
 */
@Repository
public interface PaymentPersistenceRepository extends JpaRepository<PaymentPersistenceEntity, UUID> {
    Optional<PaymentPersistenceEntity> findByTripId(UUID tripId);
}
