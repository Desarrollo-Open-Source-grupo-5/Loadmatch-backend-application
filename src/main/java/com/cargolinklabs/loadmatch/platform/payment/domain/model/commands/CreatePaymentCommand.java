package com.cargolinklabs.loadmatch.platform.payment.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Command used by a shipper to pay a completed trip
 *
 * @param tripId the trip to pay
 * @param totalAmount the total amount charged to the shipper, in soles
 * @param method the payment method
 * @param paymentToken the gateway token of the approved instrument
 * @param payerEmail the email of the authenticated shipper
 */
public record CreatePaymentCommand(UUID tripId, BigDecimal totalAmount, PaymentMethod method, String paymentToken,
                                   String payerEmail) {
    public CreatePaymentCommand {
        if (tripId == null || totalAmount == null || method == null || paymentToken == null) {
            throw new IllegalArgumentException("tripId, totalAmount, method and paymentToken cannot be null");
        }
    }
}
