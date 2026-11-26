package com.cargolinklabs.loadmatch.platform.notification.infrastructure.persistence.jpa.repositories;

import com.cargolinklabs.loadmatch.platform.notification.infrastructure.persistence.jpa.entities.NotificationPersistenceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationPersistenceRepository extends JpaRepository<NotificationPersistenceEntity, UUID> {
    List<NotificationPersistenceEntity> findByUserIdOrderByCreatedAtDescIdDesc(UUID userId);

    long countByUserIdAndReadFalse(UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select notification from NotificationPersistenceEntity notification "
            + "where notification.userId = :userId and notification.read = false order by notification.id")
    List<NotificationPersistenceEntity> findUnreadByUserIdForUpdate(@Param("userId") UUID userId);
}
