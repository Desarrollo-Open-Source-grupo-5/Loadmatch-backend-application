package com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Vehicle type persistence entity stored in the fleet schema
 */
@Entity
@Table(name = "vehicle_types", schema = "fleet")
@Getter
@Setter
@NoArgsConstructor
public class VehicleTypePersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false, unique = true, length = 60)
    private String name;

    private String description;

    @Column(nullable = false)
    private double maxWeightKg;

    @Column(nullable = false)
    private boolean active;
}
