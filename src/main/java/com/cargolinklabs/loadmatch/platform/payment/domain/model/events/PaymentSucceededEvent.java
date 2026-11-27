package com.cargolinklabs.loadmatch.platform.payment.domain.model.events;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Event raised when the gateway confirms the charge of a trip, so the carrier can be notified of the payout
 *
 * @param paymentId the payment identifier
 * @param tripId the paid trip
 * @param shipperId the shipper that paid the trip
 * @param carrierId the carrier that receives the payout
 * @param payoutAmount the amount settled to the carrier
 * @param currency the ISO 4217 currency code of the payout
 */
public record PaymentSucceededEvent(UUID paymentId, UUID tripId, UUID shipperId, UUID carrierId,
                                    BigDecimal payoutAmount, String currency) {
}
