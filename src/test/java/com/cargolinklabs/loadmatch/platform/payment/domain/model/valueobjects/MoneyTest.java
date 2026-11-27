package com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void ofSolesWithValidAmountCreatesInstance() {
        var money = Money.ofSoles(new BigDecimal("150.5"));

        assertEquals(new BigDecimal("150.50"), money.amount());
        assertEquals("PEN", money.currency());
    }

    @Test
    void constructorWithNegativeAmountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Money.ofSoles(new BigDecimal("-1")));
    }

    @Test
    void subtractWithDifferentCurrencyThrowsException() {
        var soles = Money.ofSoles(BigDecimal.TEN);
        var dollars = new Money(BigDecimal.ONE, "USD");

        assertThrows(IllegalArgumentException.class, () -> soles.subtract(dollars));
    }
}
