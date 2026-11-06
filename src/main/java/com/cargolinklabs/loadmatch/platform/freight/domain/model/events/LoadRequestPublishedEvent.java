package com.cargolinklabs.loadmatch.platform.freight.domain.model.events;

import java.util.UUID;

/**
 * Domain event raised when a load request is published and becomes visible to carriers
 *
 * @param loadRequestId the load request identifier
 * @param shipperId the identifier of the shipper that published it
 */
public record LoadRequestPublishedEvent(UUID loadRequestId, UUID shipperId) {
}
