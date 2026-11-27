package com.cargolinklabs.loadmatch.platform.trip.application.acl;

import com.cargolinklabs.loadmatch.platform.trip.application.queryservices.TripQueryService;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripByIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.acl.TripContextFacade;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.acl.TripSnapshot;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Trip context facade implementation that delegates to the local query service
 */
@Service
public class TripContextFacadeImpl implements TripContextFacade {
    private final TripQueryService tripQueryService;

    public TripContextFacadeImpl(TripQueryService tripQueryService) {
        this.tripQueryService = tripQueryService;
    }

    @Override
    public Optional<TripSnapshot> fetchTripById(UUID tripId) {
        if (tripId == null) {
            return Optional.empty();
        }
        return tripQueryService.handle(new GetTripByIdQuery(new TripId(tripId))).map(this::toSnapshot);
    }

    private TripSnapshot toSnapshot(Trip trip) {
        return new TripSnapshot(trip.getId().tripId(), trip.getLoadRequestId().loadRequestId(),
                trip.getShipperId().shipperId(), trip.getCarrierId().carrierId(), trip.getStatus().name());
    }
}
