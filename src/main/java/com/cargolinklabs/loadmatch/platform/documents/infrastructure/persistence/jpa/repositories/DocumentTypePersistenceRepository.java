package com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.entities.DocumentTypePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DocumentTypePersistenceRepository extends JpaRepository<DocumentTypePersistenceEntity, UUID> {
    List<DocumentTypePersistenceEntity> findAllByOrderByCodeAsc();

    @Modifying
    @Query(value = """
            insert into documents.document_types (id, code, name, requires_expiration, mandatory)
            values (:id, :code, :name, :requiresExpiration, :mandatory)
            on conflict (code) do nothing
            """, nativeQuery = true)
    void insertIfAbsent(@Param("id") UUID id, @Param("code") String code, @Param("name") String name,
                        @Param("requiresExpiration") boolean requiresExpiration, @Param("mandatory") boolean mandatory);
}
