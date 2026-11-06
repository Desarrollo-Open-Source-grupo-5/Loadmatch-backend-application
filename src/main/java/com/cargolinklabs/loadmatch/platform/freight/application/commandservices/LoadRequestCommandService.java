package com.cargolinklabs.loadmatch.platform.freight.application.commandservices;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.aggregates.LoadRequest;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.commands.ChangeLoadRequestStatusCommand;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.commands.CreateLoadRequestCommand;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

/**
 * Load request command service
 */
public interface LoadRequestCommandService {
    /**
     * Creates and publishes a load request
     * @param command the create load request command
     * @return the published load request or an application error
     */
    Result<LoadRequest, ApplicationError> handle(CreateLoadRequestCommand command);

    /**
     * Moves a load request to the next status of its lifecycle
     * @param command the change load request status command
     * @return the updated load request or an application error
     */
    Result<LoadRequest, ApplicationError> handle(ChangeLoadRequestStatusCommand command);
}
