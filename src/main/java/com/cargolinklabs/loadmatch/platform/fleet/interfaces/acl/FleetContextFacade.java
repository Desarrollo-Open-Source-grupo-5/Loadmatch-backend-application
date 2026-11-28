package com.cargolinklabs.loadmatch.platform.fleet.interfaces.acl;

import java.util.Optional;
import java.util.UUID;

public interface FleetContextFacade {
    Optional<String> fetchActivePlateByCarrierId(UUID carrierId);

    boolean hasActivePlate(UUID carrierId, String plate);
}
