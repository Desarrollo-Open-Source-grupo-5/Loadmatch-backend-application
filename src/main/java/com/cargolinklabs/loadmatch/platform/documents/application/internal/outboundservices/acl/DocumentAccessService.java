package com.cargolinklabs.loadmatch.platform.documents.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.iam.interfaces.acl.IamContextFacade;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DocumentAccessService {
    private final IamContextFacade iamContextFacade;
    private final ProfilesContextFacade profilesContextFacade;

    public DocumentAccessService(IamContextFacade iamContextFacade, ProfilesContextFacade profilesContextFacade) {
        this.iamContextFacade = iamContextFacade;
        this.profilesContextFacade = profilesContextFacade;
    }

    public boolean isOwner(UUID carrierId, String email) {
        return iamContextFacade.fetchUserIdByEmail(email)
                .flatMap(userId -> profilesContextFacade.fetchUserIdByCarrierId(carrierId).filter(userId::equals))
                .isPresent();
    }
}
