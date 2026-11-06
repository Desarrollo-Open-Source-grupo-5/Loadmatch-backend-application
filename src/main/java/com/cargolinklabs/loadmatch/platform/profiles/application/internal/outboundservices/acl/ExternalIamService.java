package com.cargolinklabs.loadmatch.platform.profiles.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.iam.interfaces.acl.IamContextFacade;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.UserId;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Outbound service that creates IAM user accounts on behalf of Profiles
 */
@Service
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Creates the user account that owns a profile
     * @param email the email address
     * @param password the raw password
     * @param role the role name (SHIPPER or CARRIER)
     * @return the user identifier or an application error
     */
    public Result<UserId, ApplicationError> createUser(String email, String password, String role) {
        return iamContextFacade.createUser(email, password, role).map(UserId::new);
    }
}
