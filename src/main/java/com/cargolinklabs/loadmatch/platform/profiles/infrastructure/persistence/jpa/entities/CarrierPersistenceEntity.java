package com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.EnablementStatus;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Carrier persistence entity stored in the profiles schema
 */
@Entity
@Table(name = "carriers", schema = "profiles")
@Getter
@Setter
@NoArgsConstructor
public class CarrierPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false, length = 100)
    private String firstNames;

    @Column(nullable = false, length = 100)
    private String lastNames;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(nullable = false, length = 20)
    private String licenseNumber;

    @Column(nullable = false, length = 10)
    private String licenseCategory;

    @Column(nullable = false)
    private LocalDate licenseExpirationDate;

    @Column(nullable = false, length = 9)
    private String phoneNumber;

    @Column(length = 120)
    private String operatingZone;

    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnablementStatus enablementStatus;
}
