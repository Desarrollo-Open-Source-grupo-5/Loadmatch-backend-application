package com.cargolinklabs.loadmatch.platform.notification.infrastructure.persistence.jpa.adapters;

import com.cargolinklabs.loadmatch.platform.notification.domain.model.aggregates.Notification;
import com.cargolinklabs.loadmatch.platform.notification.domain.repositories.NotificationRepository;
import com.cargolinklabs.loadmatch.platform.notification.infrastructure.persistence.jpa.assemblers.NotificationPersistenceAssembler;
import com.cargolinklabs.loadmatch.platform.notification.infrastructure.persistence.jpa.repositories.NotificationPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationPersistenceRepository persistenceRepository;

    public NotificationRepositoryImpl(NotificationPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public List<Notification> findByUserId(UUID userId) {
        return persistenceRepository.findByUserIdOrderByCreatedAtDescIdDesc(userId).stream()
                .map(NotificationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Notification> findUnreadByUserIdForUpdate(UUID userId) {
        return persistenceRepository.findUnreadByUserIdForUpdate(userId).stream()
                .map(NotificationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public long countUnread(UUID userId) {
        return persistenceRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    public void save(Notification notification) {
        persistenceRepository.save(NotificationPersistenceAssembler.toPersistenceFromDomain(notification));
    }
}
