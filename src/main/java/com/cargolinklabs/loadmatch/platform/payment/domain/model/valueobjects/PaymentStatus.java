package com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects;

/**
 * Status of a payment, from its enablement until the gateway confirms or rejects the charge
 */
public enum PaymentStatus {
    ENABLED,
    PROCESSING,
    COMPLETED,
    REJECTED
}
