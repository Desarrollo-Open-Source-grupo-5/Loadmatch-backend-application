package com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.entities.CarrierDocumentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CarrierDocumentPersistenceRepository extends JpaRepository<CarrierDocumentPersistenceEntity, UUID> {
    List<CarrierDocumentPersistenceEntity> findByCarrierIdOrderByUploadedAtDescIdDesc(UUID carrierId);
}
