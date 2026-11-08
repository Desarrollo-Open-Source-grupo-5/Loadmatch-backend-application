package com.cargolinklabs.loadmatch.platform.trip.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.CarrierId;
import org.springframework.stereotype.Service;

/**
 * Outbound service that checks carriers in the Profiles bounded context
 */
@Service("tripExternalProfilesService")
public class ExternalProfilesService {
    private final ProfilesContextFacade profilesContextFacade;

    public ExternalProfilesService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    /**
     * @param carrierId the carrier identifier
     * @return true when the carrier is enabled to accept trips
     */
    public boolean isCarrierEnabled(CarrierId carrierId) {
        return profilesContextFacade.isCarrierEnabled(carrierId.carrierId());
    }
}
