package com.cargolinklabs.loadmatch.platform.payment.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.TripId;

/**
 * Query to get the payment of a trip
 *
 * @param tripId the trip identifier
 * @param requesterEmail the email of the authenticated user, who must be the shipper or the carrier of the trip
 */
public record GetPaymentByTripIdQuery(TripId tripId, String requesterEmail) {
}
