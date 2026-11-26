package com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.IncidentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "incidents", schema = "trip")
@Getter
@Setter
public class IncidentPersistenceEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TripPersistenceEntity trip;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IncidentType type;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 500)
    private String evidenceUrl;

    @Column(nullable = false)
    private LocalDateTime reportedAt;
}
