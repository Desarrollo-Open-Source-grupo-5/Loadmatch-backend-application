package com.cargolinklabs.loadmatch.platform.payment.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.acl.TripContextFacade;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.acl.TripSnapshot;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Outbound service that queries the trip to pay in the Trip Execution bounded context
 */
@Service("paymentExternalTripService")
public class ExternalTripService {
    private final TripContextFacade tripContextFacade;

    public ExternalTripService(TripContextFacade tripContextFacade) {
        this.tripContextFacade = tripContextFacade;
    }

    public Optional<TripSnapshot> fetchTripById(TripId tripId) {
        return tripContextFacade.fetchTripById(tripId.tripId());
    }
}
