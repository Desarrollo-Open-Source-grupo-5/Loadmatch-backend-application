package com.cargolinklabs.loadmatch.platform.notification.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.notification.domain.model.valueobjects.NotificationType;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications", schema = "notification",
        indexes = @Index(name = "idx_notifications_unread_user_id", columnList = "user_id",
                options = "where is_read = false"),
        check = @CheckConstraint(name = "ck_notifications_read_at", constraint = "is_read = (read_at is not null)"))
@Getter
@Setter
public class NotificationPersistenceEntity {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false, options = "references iam.users (id)")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private boolean urgent;

    @Column(name = "is_read", nullable = false)
    private boolean read;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant readAt;
}
