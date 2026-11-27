package com.cargolinklabs.loadmatch.platform.trip.interfaces.acl;

import java.util.Optional;
import java.util.UUID;

/**
 * Trip context facade
 * <p>
 *     Entry point that other bounded contexts use to query trips by identity.
 * </p>
 */
public interface TripContextFacade {
    /**
     * @param tripId the trip identifier
     * @return the trip snapshot, or empty when the trip does not exist
     */
    Optional<TripSnapshot> fetchTripById(UUID tripId);
}
