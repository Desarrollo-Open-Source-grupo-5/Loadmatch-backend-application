package com.cargolinklabs.loadmatch.platform.freight.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestStatus;
import com.cargolinklabs.loadmatch.platform.freight.infrastructure.persistence.jpa.entities.LoadRequestPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for load request persistence entities
 */
@Repository
public interface LoadRequestPersistenceRepository extends JpaRepository<LoadRequestPersistenceEntity, UUID> {
    List<LoadRequestPersistenceEntity> findByShipperIdOrderByCreatedAtDesc(UUID shipperId);

    List<LoadRequestPersistenceEntity> findByStatusOrderByPublishedAtDesc(LoadRequestStatus status);
}
