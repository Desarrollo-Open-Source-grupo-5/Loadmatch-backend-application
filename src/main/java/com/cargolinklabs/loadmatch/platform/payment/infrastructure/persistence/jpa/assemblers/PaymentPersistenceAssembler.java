package com.cargolinklabs.loadmatch.platform.payment.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates.Payment;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.payment.infrastructure.persistence.jpa.entities.PaymentPersistenceEntity;

/**
 * Assembler that maps payments between the domain model and the persistence entity
 */
public final class PaymentPersistenceAssembler {

    private PaymentPersistenceAssembler() {
    }

    public static Payment toDomainFromPersistence(PaymentPersistenceEntity entity) {
        var currency = entity.getCurrency();
        var gatewayReference = entity.getGatewayReference() == null ? null
                : new GatewayReference(entity.getGatewayReference(), entity.getProcessedAt());
        return Payment.reconstitute(new PaymentId(entity.getId()), new TripId(entity.getTripId()),
                new ShipperId(entity.getShipperId()), new CarrierId(entity.getCarrierId()),
                new Money(entity.getTotalAmount(), currency), new Money(entity.getPlatformFee(), currency),
                new Money(entity.getPayoutAmount(), currency), entity.getMethod(), entity.getStatus(),
                gatewayReference, entity.getErrorCode(), entity.getAttempts(), entity.getEnabledAt(),
                entity.getProcessedAt());
    }

    public static PaymentPersistenceEntity toPersistenceFromDomain(Payment payment) {
        var entity = new PaymentPersistenceEntity();
        entity.setId(payment.getId().paymentId());
        entity.setTripId(payment.getTripId().tripId());
        entity.setShipperId(payment.getShipperId().shipperId());
        entity.setCarrierId(payment.getCarrierId().carrierId());
        entity.setTotalAmount(payment.getTotalAmount().amount());
        entity.setPlatformFee(payment.getPlatformFee().amount());
        entity.setPayoutAmount(payment.getPayoutAmount().amount());
        entity.setCurrency(payment.getTotalAmount().currency());
        entity.setMethod(payment.getMethod());
        entity.setStatus(payment.getStatus());
        entity.setGatewayReference(payment.getGatewayReference() == null ? null : payment.getGatewayReference().value());
        entity.setErrorCode(payment.getErrorCode());
        entity.setAttempts(payment.getAttempts());
        entity.setEnabledAt(payment.getEnabledAt());
        entity.setProcessedAt(payment.getProcessedAt());
        return entity;
    }
}
