package com.cargolinklabs.loadmatch.platform.fleet.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

/**
 * Outbound service that queries carriers in the Profiles bounded context
 */
@Service("fleetExternalProfilesService")
public class ExternalProfilesService {
    private final ProfilesContextFacade profilesContextFacade;

    public ExternalProfilesService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    /**
     * @param carrierId the carrier identifier
     * @return true when the carrier exists
     */
    public boolean existsCarrier(CarrierId carrierId) {
        return profilesContextFacade.existsCarrierById(carrierId.carrierId());
    }
}
