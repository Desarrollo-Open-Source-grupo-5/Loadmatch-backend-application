package com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.events.PaymentSucceededEvent;
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

    /**
     * Starts the charge with the instrument approved by the shipper in the gateway
     * @param method the payment method
     * @param token the gateway token of the approved instrument
     */
    public void process(PaymentMethod method, PaymentToken token) {
        ensureStatus(PaymentStatus.ENABLED, PaymentStatus.PROCESSING);
        if (method == null || token == null) {
            throw new IllegalArgumentException("Payment method and token are required");
        }
        this.method = method;
        this.status = PaymentStatus.PROCESSING;
        this.attempts++;
    }

    /**
     * The gateway confirmed the charge, so the payment is completed and the carrier payout is announced
     * @param reference the reference of the confirmed transaction
     */
    public void confirm(GatewayReference reference) {
        ensureStatus(PaymentStatus.PROCESSING, PaymentStatus.COMPLETED);
        if (reference == null) {
            throw new IllegalArgumentException("Gateway reference is required to complete the payment");
        }
        this.gatewayReference = reference;
        this.processedAt = reference.processedAt();
        this.status = PaymentStatus.COMPLETED;
        registerDomainEvent(new PaymentSucceededEvent(id.paymentId(), tripId.tripId(), shipperId.shipperId(),
                carrierId.carrierId(), payoutAmount.amount(), payoutAmount.currency()));
    }

    /**
     * The gateway rejected the charge
     * @param errorCode the error code returned by the gateway
     */
    public void reject(String errorCode) {
        ensureStatus(PaymentStatus.PROCESSING, PaymentStatus.REJECTED);
        if (errorCode == null || errorCode.isBlank()) {
            throw new IllegalArgumentException("Gateway error code is required to reject the payment");
        }
        this.errorCode = errorCode;
        this.processedAt = LocalDateTime.now();
        this.status = PaymentStatus.REJECTED;
    }

    /**
     * Enables a rejected payment again so the shipper can retry with another instrument
     */
    public void retry() {
        ensureStatus(PaymentStatus.REJECTED, PaymentStatus.ENABLED);
        this.errorCode = null;
        this.processedAt = null;
        this.status = PaymentStatus.ENABLED;
    }

    public boolean isCompleted() {
        return this.status == PaymentStatus.COMPLETED;
    }

    private void ensureStatus(PaymentStatus expected, PaymentStatus target) {
        if (this.status != expected) {
            throw new IllegalStateException("A payment cannot move from %s to %s".formatted(this.status, target));
        }
    }

    private static Money calculateFee(Money totalAmount, BigDecimal platformFeeRate) {
        if (platformFeeRate == null || platformFeeRate.signum() < 0 || platformFeeRate.compareTo(BigDecimal.ONE) >= 0) {
            throw new IllegalArgumentException("Platform fee rate must be between 0 and 1");
        }
        return totalAmount.multiply(platformFeeRate);
    }
}
