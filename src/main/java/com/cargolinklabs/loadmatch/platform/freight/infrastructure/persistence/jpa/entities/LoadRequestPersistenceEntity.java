package com.cargolinklabs.loadmatch.platform.freight.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestStatus;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Load request persistence entity stored in the freight schema
 * <p>
 *     The route, dimensions and rate value objects are stored as embedded columns with a prefix.
 * </p>
 */
@Entity
@Table(name = "load_requests", schema = "freight")
@Getter
@Setter
@NoArgsConstructor
public class LoadRequestPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false)
    private UUID shipperId;

    @Column(nullable = false)
    private UUID vehicleTypeId;

    @Column(nullable = false)
    private String originAddress;

    @Column(nullable = false, length = 80)
    private String originDistrict;

    @Column(nullable = false)
    private double originLatitude;

    @Column(nullable = false)
    private double originLongitude;

    @Column(nullable = false)
    private String destinationAddress;

    @Column(nullable = false, length = 80)
    private String destinationDistrict;

    @Column(nullable = false)
    private double destinationLatitude;

    @Column(nullable = false)
    private double destinationLongitude;

    @Column(nullable = false)
    private double weightKg;

    @Column(nullable = false)
    private double lengthM;

    @Column(nullable = false)
    private double widthM;

    @Column(nullable = false)
    private double heightM;

    @Column(nullable = false, length = 80)
    private String cargoType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal rateAmount;

    @Column(nullable = false, length = 3)
    private String rateCurrency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoadRequestStatus status;

    @Column(nullable = false)
    private LocalDateTime pickupAt;

    private LocalDateTime publishedAt;

    @Column(nullable = false)
    private boolean urgent;
}
