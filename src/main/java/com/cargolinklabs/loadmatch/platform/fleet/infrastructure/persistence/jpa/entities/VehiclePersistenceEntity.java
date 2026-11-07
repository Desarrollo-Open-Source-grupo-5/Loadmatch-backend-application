package com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.MtcValidationStatus;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Vehicle persistence entity stored in the fleet schema
 */
@Entity
@Table(name = "vehicles", schema = "fleet")
@Getter
@Setter
@NoArgsConstructor
public class VehiclePersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false)
    private UUID carrierId;

    @Column(nullable = false)
    private UUID vehicleTypeId;

    @Column(nullable = false, unique = true, length = 7)
    private String licensePlate;

    @Column(nullable = false, length = 60)
    private String brand;

    @Column(nullable = false, length = 60)
    private String model;

    @Column(nullable = false)
    private int manufactureYear;

    @Column(nullable = false)
    private double payloadKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MtcValidationStatus mtcValidationStatus;

    @Column(nullable = false)
    private boolean active;
}
