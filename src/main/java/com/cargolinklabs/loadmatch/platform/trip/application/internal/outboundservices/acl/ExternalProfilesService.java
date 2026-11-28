package com.cargolinklabs.loadmatch.platform.trip.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.iam.interfaces.acl.IamContextFacade;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates.Trip;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.CarrierId;
import org.springframework.stereotype.Service;

/**
 * Outbound service that checks carriers in the Profiles bounded context
 */
@Service("tripExternalProfilesService")
public class ExternalProfilesService {
    private final ProfilesContextFacade profilesContextFacade;
    private final IamContextFacade iamContextFacade;

    public ExternalProfilesService(ProfilesContextFacade profilesContextFacade, IamContextFacade iamContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * @param carrierId the carrier identifier
     * @return true when the carrier is enabled to accept trips
     */
    public boolean isCarrierEnabled(CarrierId carrierId) {
        return profilesContextFacade.isCarrierEnabled(carrierId.carrierId());
    }

    public boolean isCarrierOwner(CarrierId carrierId, String email) {
        return iamContextFacade.fetchUserIdByEmail(email)
                .flatMap(userId -> profilesContextFacade.fetchUserIdByCarrierId(carrierId.carrierId())
                        .filter(userId::equals))
                .isPresent();
    }

    public boolean isAssignedCarrier(Trip trip, String email) {
        return isCarrierOwner(trip.getCarrierId(), email);
    }

    public boolean isParticipant(Trip trip, String email) {
        return iamContextFacade.fetchUserIdByEmail(email)
                .filter(userId -> profilesContextFacade.fetchUserIdByShipperId(trip.getShipperId().shipperId())
                        .filter(userId::equals).isPresent()
                        || profilesContextFacade.fetchUserIdByCarrierId(trip.getCarrierId().carrierId())
                        .filter(userId::equals).isPresent())
                .isPresent();
    }
}
