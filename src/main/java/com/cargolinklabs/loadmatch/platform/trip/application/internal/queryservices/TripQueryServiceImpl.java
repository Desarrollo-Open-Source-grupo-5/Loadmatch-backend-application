package com.cargolinklabs.loadmatch.platform.trip.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.trip.application.queryservices.TripQueryService;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripByIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripsByCarrierIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripsByShipperIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.repositories.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Trip query service implementation
 */
@Service
public class TripQueryServiceImpl implements TripQueryService {
    private final TripRepository tripRepository;

    public TripQueryServiceImpl(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public Optional<Trip> handle(GetTripByIdQuery query) {
        return tripRepository.findById(query.tripId());
    }

    @Override
    public List<Trip> handle(GetTripsByCarrierIdQuery query) {
        return tripRepository.findByCarrierId(query.carrierId());
    }

    @Override
    public List<Trip> handle(GetTripsByShipperIdQuery query) {
        return tripRepository.findByShipperId(query.shipperId());
    }
}
