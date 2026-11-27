package com.cargolinklabs.loadmatch.platform.payment.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Payment resource sent by a shipper with the identifier of the order approved in PayPal
 */
public record CreatePaymentResource(
        @NotNull(message = "{validation.not-blank}") UUID tripId,
        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{payment.amount.positive}") BigDecimal totalAmount,
        @NotBlank(message = "{validation.not-blank}")
        @Pattern(regexp = "CARD|PAYPAL", message = "{payment.method.invalid}") String method,
        @NotBlank(message = "{validation.not-blank}") String paymentToken) {
}
