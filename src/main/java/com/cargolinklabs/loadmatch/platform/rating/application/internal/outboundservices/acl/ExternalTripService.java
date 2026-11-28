package com.cargolinklabs.loadmatch.platform.rating.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.trip.interfaces.acl.TripContextFacade;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.acl.TripSnapshot;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Outbound ACL service wrapping TripContextFacade
 */
@Service
public class ExternalTripService {
    private final TripContextFacade tripContextFacade;

    public ExternalTripService(TripContextFacade tripContextFacade) {
        this.tripContextFacade = tripContextFacade;
    }

    public record TripData(UUID tripId, UUID loadRequestId, UUID shipperId, UUID carrierId, String status) {
    }

    public Optional<TripData> fetchTripById(UUID tripId) {
        return tripContextFacade.fetchTripById(tripId)
                .map(this::toTripData);
    }

    private TripData toTripData(TripSnapshot snapshot) {
        return new TripData(
                snapshot.tripId(),
                snapshot.loadRequestId(),
                snapshot.shipperId(),
                snapshot.carrierId(),
                snapshot.status()
        );
    }
}
