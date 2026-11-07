package com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.entities.VehiclePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for vehicle persistence entities
 */
@Repository
public interface VehiclePersistenceRepository extends JpaRepository<VehiclePersistenceEntity, UUID> {
    List<VehiclePersistenceEntity> findByCarrierIdOrderByCreatedAtAsc(UUID carrierId);

    boolean existsByLicensePlate(String licensePlate);
}
