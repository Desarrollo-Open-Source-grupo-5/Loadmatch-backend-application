package com.cargolinklabs.loadmatch.platform.payment.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.PaymentMethod;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.PaymentStatus;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payment persistence entity stored in the payment schema
 */
@Entity
@Table(name = "payments", schema = "payment")
@Getter
@Setter
@NoArgsConstructor
public class PaymentPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false, unique = true)
    private UUID tripId;

    @Column(nullable = false)
    private UUID shipperId;

    @Column(nullable = false)
    private UUID carrierId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal platformFee;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal payoutAmount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(length = 120)
    private String gatewayReference;

    @Column(length = 60)
    private String errorCode;

    @Column(nullable = false)
    private int attempts;

    @Column(nullable = false)
    private LocalDateTime enabledAt;

    private LocalDateTime processedAt;
}
