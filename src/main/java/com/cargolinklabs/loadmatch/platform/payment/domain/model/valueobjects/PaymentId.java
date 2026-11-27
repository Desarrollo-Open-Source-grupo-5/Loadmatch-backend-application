package com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects;

import java.util.UUID;

/**
 * Payment identifier value object
 *
 * @param paymentId the UUID identifier
 */
public record PaymentId(UUID paymentId) {
    public PaymentId {
        if (paymentId == null) {
            throw new IllegalArgumentException("PaymentId cannot be null");
        }
    }

    /**
     * Creates a new random identifier
     */
    public PaymentId() {
        this(UUID.randomUUID());
    }
}
