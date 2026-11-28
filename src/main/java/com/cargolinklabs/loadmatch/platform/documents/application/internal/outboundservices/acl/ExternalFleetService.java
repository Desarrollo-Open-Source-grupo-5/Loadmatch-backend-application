package com.cargolinklabs.loadmatch.platform.documents.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.fleet.interfaces.acl.FleetContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("documentsExternalFleetService")
public class ExternalFleetService {
    private final FleetContextFacade fleetContextFacade;

    public ExternalFleetService(FleetContextFacade fleetContextFacade) {
        this.fleetContextFacade = fleetContextFacade;
    }

    public boolean hasActivePlate(UUID carrierId, String plate) {
        return fleetContextFacade.hasActivePlate(carrierId, plate);
    }
}
