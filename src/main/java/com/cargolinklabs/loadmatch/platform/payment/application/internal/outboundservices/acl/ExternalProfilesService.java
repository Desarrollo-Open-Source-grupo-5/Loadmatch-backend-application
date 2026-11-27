package com.cargolinklabs.loadmatch.platform.payment.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.iam.interfaces.acl.IamContextFacade;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Outbound service that checks whether the authenticated user is the shipper or the carrier of a trip
 */
@Service("paymentExternalProfilesService")
public class ExternalProfilesService {
    private final ProfilesContextFacade profilesContextFacade;
    private final IamContextFacade iamContextFacade;

    public ExternalProfilesService(ProfilesContextFacade profilesContextFacade, IamContextFacade iamContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
        this.iamContextFacade = iamContextFacade;
    }

    public boolean isShipper(UUID shipperId, String email) {
        return userIdOf(email).filter(userId -> profilesContextFacade.fetchUserIdByShipperId(shipperId)
                .filter(userId::equals).isPresent()).isPresent();
    }

    public boolean isCarrier(UUID carrierId, String email) {
        return userIdOf(email).filter(userId -> profilesContextFacade.fetchUserIdByCarrierId(carrierId)
                .filter(userId::equals).isPresent()).isPresent();
    }

    private Optional<UUID> userIdOf(String email) {
        return email == null || email.isBlank() ? Optional.empty() : iamContextFacade.fetchUserIdByEmail(email);
    }
}
