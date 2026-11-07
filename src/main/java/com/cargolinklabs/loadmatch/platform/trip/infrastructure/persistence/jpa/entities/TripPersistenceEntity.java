package com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Trip persistence entity stored in the trip schema
 */
@Entity
@Table(name = "trips", schema = "trip")
@Getter
@Setter
@NoArgsConstructor
public class TripPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false, unique = true)
    private UUID loadRequestId;

    @Column(nullable = false)
    private UUID shipperId;

    @Column(nullable = false)
    private UUID carrierId;

    @Column(nullable = false)
    private UUID vehicleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TripStatus status;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    private LocalDateTime deliveredAt;

    private String deliveryEvidenceUrl;

    @Column(length = 500)
    private String deliveryObservation;

    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("registeredAt ASC")
    private List<TripStatusHistoryPersistenceEntity> history = new ArrayList<>();
}
