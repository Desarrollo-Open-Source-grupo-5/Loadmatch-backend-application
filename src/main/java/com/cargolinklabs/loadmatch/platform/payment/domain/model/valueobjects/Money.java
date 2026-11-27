package com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Money value object used to quantify the charge, the platform fee and the payout
 *
 * @param amount the amount, zero or greater, rounded to two decimals
 * @param currency the ISO 4217 currency code (PEN by default)
 */
public record Money(BigDecimal amount, String currency) {
    private static final String DEFAULT_CURRENCY = "PEN";

    public Money {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Amount cannot be null nor negative");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        currency = currency == null || currency.isBlank() ? DEFAULT_CURRENCY : currency.trim().toUpperCase();
    }

    /**
     * Creates an amount in Peruvian soles
     */
    public static Money ofSoles(BigDecimal amount) {
        return new Money(amount, DEFAULT_CURRENCY);
    }

    public static Money zero() {
        return ofSoles(BigDecimal.ZERO);
    }

    public Money add(Money other) {
        ensureSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other) {
        ensureSameCurrency(other);
        return new Money(amount.subtract(other.amount), currency);
    }

    public Money multiply(BigDecimal factor) {
        if (factor == null) {
            throw new IllegalArgumentException("Factor cannot be null");
        }
        return new Money(amount.multiply(factor), currency);
    }

    private void ensureSameCurrency(Money other) {
        if (other == null || !currency.equals(other.currency)) {
            throw new IllegalArgumentException("Money operations require the same currency");
        }
    }
}
