package com.cargolinklabs.loadmatch.platform.payment.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Payment resource returned to the shipper and the carrier of the trip
 */
public record PaymentResource(UUID id, UUID tripId, UUID shipperId, UUID carrierId, BigDecimal totalAmount,
                              BigDecimal platformFee, BigDecimal payoutAmount, String currency, String method,
                              String status, String gatewayReference, String errorCode, int attempts,
                              LocalDateTime enabledAt, LocalDateTime processedAt) {
}
