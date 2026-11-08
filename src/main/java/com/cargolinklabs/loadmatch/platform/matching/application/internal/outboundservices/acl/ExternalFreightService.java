package com.cargolinklabs.loadmatch.platform.matching.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.freight.interfaces.acl.FreightContextFacade;
import com.cargolinklabs.loadmatch.platform.freight.interfaces.acl.PublishedLoadRequestSnapshot;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Outbound service that reads published load requests from the Freight Publishing bounded context
 */
@Service("matchingExternalFreightService")
public class ExternalFreightService {
    private final FreightContextFacade freightContextFacade;

    public ExternalFreightService(FreightContextFacade freightContextFacade) {
        this.freightContextFacade = freightContextFacade;
    }

    /**
     * @return the load requests that are published and available
     */
    public List<PublishedLoadRequestSnapshot> fetchPublishedLoadRequests() {
        return freightContextFacade.fetchPublishedLoadRequests();
    }
}
