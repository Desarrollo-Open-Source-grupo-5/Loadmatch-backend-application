package com.cargolinklabs.loadmatch.platform.trip.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.freight.interfaces.acl.FreightContextFacade;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.LoadRequestId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.ShipperId;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Outbound service that reads and updates load requests in the Freight Publishing bounded context
 */
@Service("tripExternalFreightService")
public class ExternalFreightService {
    private final FreightContextFacade freightContextFacade;

    public ExternalFreightService(FreightContextFacade freightContextFacade) {
        this.freightContextFacade = freightContextFacade;
    }

    public boolean isLoadRequestAvailable(LoadRequestId loadRequestId) {
        return freightContextFacade.isLoadRequestAvailable(loadRequestId.loadRequestId());
    }

    public Optional<ShipperId> fetchShipperId(LoadRequestId loadRequestId) {
        return freightContextFacade.fetchShipperIdByLoadRequestId(loadRequestId.loadRequestId()).map(ShipperId::new);
    }

    public boolean markLoadRequestAsAssigned(LoadRequestId loadRequestId) {
        return freightContextFacade.markLoadRequestAsAssigned(loadRequestId.loadRequestId());
    }

    public boolean markLoadRequestInTransit(LoadRequestId loadRequestId) {
        return freightContextFacade.markLoadRequestInTransit(loadRequestId.loadRequestId());
    }

    public boolean markLoadRequestAsDelivered(LoadRequestId loadRequestId) {
        return freightContextFacade.markLoadRequestAsDelivered(loadRequestId.loadRequestId());
    }
}
