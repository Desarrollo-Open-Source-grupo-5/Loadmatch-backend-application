package com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Trip status history persistence entity stored in the trip schema
 */
@Entity
@Table(name = "trip_status_histories", schema = "trip")
@Getter
@Setter
@NoArgsConstructor
public class TripStatusHistoryPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private TripPersistenceEntity trip;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private TripStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TripStatus newStatus;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private LocalDateTime registeredAt;
}
