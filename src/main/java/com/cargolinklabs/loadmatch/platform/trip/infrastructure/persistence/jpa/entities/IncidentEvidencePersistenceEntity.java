package com.cargolinklabs.loadmatch.platform.trip.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Table(name = "incident_evidence", schema = "trip")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IncidentEvidencePersistenceEntity {
    @Id
    private UUID incidentId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "incident_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private IncidentPersistenceEntity incident;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 32)
    private String contentType;

    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] content;

    public IncidentEvidencePersistenceEntity(IncidentPersistenceEntity incident, String fileName, String contentType,
                                             byte[] content) {
        this.incident = incident;
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
    }
}
