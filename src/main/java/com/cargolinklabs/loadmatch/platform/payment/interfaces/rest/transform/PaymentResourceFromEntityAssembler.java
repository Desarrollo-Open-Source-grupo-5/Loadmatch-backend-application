package com.cargolinklabs.loadmatch.platform.payment.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates.Payment;
import com.cargolinklabs.loadmatch.platform.payment.interfaces.rest.resources.PaymentResource;

/**
 * Assembler that translates a payment into its resource
 */
public final class PaymentResourceFromEntityAssembler {

    private PaymentResourceFromEntityAssembler() {
    }

    public static PaymentResource toResourceFromEntity(Payment payment) {
        return new PaymentResource(payment.getId().paymentId(), payment.getTripId().tripId(),
                payment.getShipperId().shipperId(), payment.getCarrierId().carrierId(),
                payment.getTotalAmount().amount(), payment.getPlatformFee().amount(),
                payment.getPayoutAmount().amount(), payment.getTotalAmount().currency(),
                payment.getMethod() == null ? null : payment.getMethod().name(), payment.getStatus().name(),
                payment.getGatewayReference() == null ? null : payment.getGatewayReference().value(),
                payment.getErrorCode(), payment.getAttempts(), payment.getEnabledAt(), payment.getProcessedAt());
    }
}
