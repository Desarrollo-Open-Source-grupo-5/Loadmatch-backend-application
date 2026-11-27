package com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment aggregate root
 * <p>
 *     Charges a completed trip to the shipper and splits the total amount into the platform fee and the payout
 *     to the carrier. A trip has a single payment, which can be retried after the gateway rejects the charge.
 * </p>
 */
@Getter
public class Payment extends AbstractDomainAggregateRoot<Payment> {
    private PaymentId id;
    private TripId tripId;
    private ShipperId shipperId;
    private CarrierId carrierId;
    private Money totalAmount;
    private Money platformFee;
    private Money payoutAmount;
    private PaymentMethod method;
    private PaymentStatus status;
    private GatewayReference gatewayReference;
    private String errorCode;
    private int attempts;
    private LocalDateTime enabledAt;
    private LocalDateTime processedAt;

    public Payment(TripId tripId, ShipperId shipperId, CarrierId carrierId, Money totalAmount,
                   BigDecimal platformFeeRate) {
        if (tripId == null || shipperId == null || carrierId == null) {
            throw new IllegalArgumentException("Trip, shipper and carrier are required");
        }
        if (totalAmount == null || totalAmount.amount().signum() <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }
        this.id = new PaymentId();
        this.tripId = tripId;
        this.shipperId = shipperId;
        this.carrierId = carrierId;
        this.totalAmount = totalAmount;
        this.platformFee = calculateFee(totalAmount, platformFeeRate);
        this.payoutAmount = totalAmount.subtract(this.platformFee);
        this.status = PaymentStatus.ENABLED;
        this.enabledAt = LocalDateTime.now();
    }

    private Payment() {
    }

    /**
     * Rebuilds a payment from its persisted state without raising domain events
     */
    public static Payment reconstitute(PaymentId id, TripId tripId, ShipperId shipperId, CarrierId carrierId,
                                       Money totalAmount, Money platformFee, Money payoutAmount,
                                       PaymentMethod method, PaymentStatus status, GatewayReference gatewayReference,
                                       String errorCode, int attempts, LocalDateTime enabledAt,
                                       LocalDateTime processedAt) {
        var payment = new Payment();
        payment.id = id;
        payment.tripId = tripId;
        payment.shipperId = shipperId;
        payment.carrierId = carrierId;
        payment.totalAmount = totalAmount;
        payment.platformFee = platformFee;
        payment.payoutAmount = payoutAmount;
        payment.method = method;
        payment.status = status;
        payment.gatewayReference = gatewayReference;
        payment.errorCode = errorCode;
        payment.attempts = attempts;
        payment.enabledAt = enabledAt;
        payment.processedAt = processedAt;
        return payment;
    }

    private static Money calculateFee(Money totalAmount, BigDecimal platformFeeRate) {
        if (platformFeeRate == null || platformFeeRate.signum() < 0 || platformFeeRate.compareTo(BigDecimal.ONE) >= 0) {
            throw new IllegalArgumentException("Platform fee rate must be between 0 and 1");
        }
        return totalAmount.multiply(platformFeeRate);
    }
}
