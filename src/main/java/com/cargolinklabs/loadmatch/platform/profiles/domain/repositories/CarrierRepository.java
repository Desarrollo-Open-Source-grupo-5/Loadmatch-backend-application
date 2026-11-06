package com.cargolinklabs.loadmatch.platform.profiles.domain.repositories;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Carrier;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.Dni;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.UserId;

import java.util.Optional;

/**
 * Carrier repository port
 */
public interface CarrierRepository {
    Optional<Carrier> findById(CarrierId id);

    Optional<Carrier> findByUserId(UserId userId);

    boolean existsByDni(Dni dni);

    Carrier save(Carrier carrier);
}
