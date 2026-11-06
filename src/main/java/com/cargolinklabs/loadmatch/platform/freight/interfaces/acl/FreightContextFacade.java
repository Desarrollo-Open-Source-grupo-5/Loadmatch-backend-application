package com.cargolinklabs.loadmatch.platform.freight.interfaces.acl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Freight Publishing context facade
 * <p>
 *     Entry point that Matching and Trip Execution use to read and update load requests by identity.
 * </p>
 */
public interface FreightContextFacade {
    /**
     * @return the load requests that are published and available for carriers
     */
    List<PublishedLoadRequestSnapshot> fetchPublishedLoadRequests();

    /**
     * @param loadRequestId the load request identifier
     * @return true when the load request exists and is still available
     */
    boolean isLoadRequestAvailable(UUID loadRequestId);

    /**
     * @param loadRequestId the load request identifier
     * @return the identifier of the shipper that published the load request
     */
    Optional<UUID> fetchShipperIdByLoadRequestId(UUID loadRequestId);

    /**
     * @param loadRequestId the load request identifier
     * @return true when the load request was marked as assigned
     */
    boolean markLoadRequestAsAssigned(UUID loadRequestId);

    /**
     * @param loadRequestId the load request identifier
     * @return true when the load request was marked as in transit
     */
    boolean markLoadRequestInTransit(UUID loadRequestId);

    /**
     * @param loadRequestId the load request identifier
     * @return true when the load request was marked as delivered
     */
    boolean markLoadRequestAsDelivered(UUID loadRequestId);
}
