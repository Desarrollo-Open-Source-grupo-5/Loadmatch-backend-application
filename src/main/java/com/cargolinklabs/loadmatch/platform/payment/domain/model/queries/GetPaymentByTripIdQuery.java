package com.cargolinklabs.loadmatch.platform.payment.domain.model.queries;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.TripId;

/**
 * Query to get the payment of a trip
 *
 * @param tripId the trip identifier
 */
public record GetPaymentByTripIdQuery(TripId tripId) {
}
