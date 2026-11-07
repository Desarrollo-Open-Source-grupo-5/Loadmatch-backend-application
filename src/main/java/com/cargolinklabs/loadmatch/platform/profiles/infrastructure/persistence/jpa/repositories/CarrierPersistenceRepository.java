package com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.entities.CarrierPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for carrier persistence entities
 */
@Repository
public interface CarrierPersistenceRepository extends JpaRepository<CarrierPersistenceEntity, UUID> {
    Optional<CarrierPersistenceEntity> findByUserId(UUID userId);

    boolean existsByDni(String dni);
}
