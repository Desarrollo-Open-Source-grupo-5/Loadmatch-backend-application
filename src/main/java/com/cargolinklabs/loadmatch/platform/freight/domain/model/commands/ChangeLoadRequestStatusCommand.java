package com.cargolinklabs.loadmatch.platform.freight.domain.model.commands;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestId;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestStatus;

/**
 * Command used by other bounded contexts (Trip Execution) to move a load request through its lifecycle
 *
 * @param loadRequestId the load request identifier
 * @param targetStatus the target status (ASSIGNED, IN_TRANSIT or DELIVERED)
 */
public record ChangeLoadRequestStatusCommand(LoadRequestId loadRequestId, LoadRequestStatus targetStatus) {
    public ChangeLoadRequestStatusCommand {
        if (loadRequestId == null || targetStatus == null) {
            throw new IllegalArgumentException("loadRequestId and targetStatus cannot be null");
        }
    }
}
