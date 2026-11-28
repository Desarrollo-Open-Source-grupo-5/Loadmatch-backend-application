package com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.ValidationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "carrier_documents", schema = "documents",
        indexes = @Index(columnList = "carrier_id, uploaded_at"))
@Getter
@Setter
@NoArgsConstructor
public class CarrierDocumentPersistenceEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID carrierId;

    @Column(nullable = false)
    private UUID documentTypeId;

    @Column(nullable = false, length = 100)
    private String documentNumber;

    @Column(length = 20)
    private String licensePlate;

    @Column(nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false, length = 255)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ValidationStatus status;

    @Column(length = 40)
    private String rejectionReason;

    private LocalDate expiresAt;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    private LocalDateTime validatedAt;
}
