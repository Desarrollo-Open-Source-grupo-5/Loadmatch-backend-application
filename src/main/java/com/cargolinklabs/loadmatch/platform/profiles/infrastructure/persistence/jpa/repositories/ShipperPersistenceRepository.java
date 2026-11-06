package com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.entities.ShipperPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for shipper persistence entities
 */
@Repository
public interface ShipperPersistenceRepository extends JpaRepository<ShipperPersistenceEntity, UUID> {
    Optional<ShipperPersistenceEntity> findByUserId(UUID userId);

    boolean existsByRuc(String ruc);
}
