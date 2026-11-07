package com.cargolinklabs.loadmatch.platform.trip.domain.repositories;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Trip repository port
 */
public interface TripRepository {
    Optional<Trip> findById(TripId id);

    List<Trip> findByCarrierId(CarrierId carrierId);

    List<Trip> findByShipperId(ShipperId shipperId);

    Trip save(Trip trip);

    List<Trip> findDeliveredBefore(LocalDateTime deliveredAt);
}
