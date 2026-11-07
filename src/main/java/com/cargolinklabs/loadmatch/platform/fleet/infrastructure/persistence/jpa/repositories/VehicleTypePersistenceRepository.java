package com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.entities.VehicleTypePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for vehicle type persistence entities
 */
@Repository
public interface VehicleTypePersistenceRepository extends JpaRepository<VehicleTypePersistenceEntity, UUID> {
    List<VehicleTypePersistenceEntity> findByActiveTrueOrderByMaxWeightKgAsc();

    boolean existsByName(String name);
}
