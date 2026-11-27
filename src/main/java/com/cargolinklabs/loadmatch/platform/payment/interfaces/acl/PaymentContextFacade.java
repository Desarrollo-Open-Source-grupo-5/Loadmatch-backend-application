package com.cargolinklabs.loadmatch.platform.payment.interfaces.acl;

import java.util.UUID;

/**
 * Payment context facade
 * <p>
 *     Entry point that other bounded contexts use to query the payment of a trip.
 * </p>
 */
public interface PaymentContextFacade {
    /**
     * @param tripId the trip identifier
     * @return true when the trip has a completed payment
     */
    boolean isTripPaid(UUID tripId);
}
