package com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects;

import java.math.BigDecimal;

/**
 * Money value object
 *
 * @param amount the amount, greater than zero
 * @param currency the ISO 4217 currency code (PEN by default)
 */
public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        currency = currency == null || currency.isBlank() ? "PEN" : currency.trim().toUpperCase();
    }

    /**
     * Creates an amount in Peruvian soles
     */
    public static Money ofSoles(BigDecimal amount) {
        return new Money(amount, "PEN");
    }
}
