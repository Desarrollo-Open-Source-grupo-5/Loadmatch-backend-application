package com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects;

/**
 * Token that represents the payment instrument approved in the gateway
 * <p>
 *     Only the token reaches the gateway: no card data is handled nor persisted by the platform.
 * </p>
 *
 * @param value the gateway token (the identifier of the approved PayPal order)
 */
public record PaymentToken(String value) {
    public PaymentToken {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Payment token cannot be blank");
        }
        value = value.trim();
    }
}
