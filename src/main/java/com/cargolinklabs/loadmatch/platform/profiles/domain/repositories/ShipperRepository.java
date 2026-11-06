package com.cargolinklabs.loadmatch.platform.profiles.domain.repositories;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Shipper;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.Ruc;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.UserId;

import java.util.Optional;

/**
 * Shipper repository port
 */
public interface ShipperRepository {
    Optional<Shipper> findById(ShipperId id);

    Optional<Shipper> findByUserId(UserId userId);

    boolean existsByRuc(Ruc ruc);

    Shipper save(Shipper shipper);
}
