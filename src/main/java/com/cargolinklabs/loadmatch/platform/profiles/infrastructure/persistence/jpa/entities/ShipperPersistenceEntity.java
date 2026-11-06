package com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Shipper persistence entity stored in the profiles schema
 */
@Entity
@Table(name = "shippers", schema = "profiles")
@Getter
@Setter
@NoArgsConstructor
public class ShipperPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false, unique = true, length = 11)
    private String ruc;

    @Column(nullable = false, length = 150)
    private String businessName;

    @Column(nullable = false, length = 120)
    private String contactName;

    @Column(nullable = false, length = 9)
    private String phoneNumber;

    @Column(nullable = false)
    private String fiscalStreet;

    @Column(nullable = false, length = 80)
    private String fiscalDistrict;

    @Column(nullable = false, length = 80)
    private String fiscalProvince;

    @Column(nullable = false, length = 80)
    private String fiscalDepartment;
}
