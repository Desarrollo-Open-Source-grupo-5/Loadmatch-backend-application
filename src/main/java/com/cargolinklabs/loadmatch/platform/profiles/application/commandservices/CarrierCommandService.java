package com.cargolinklabs.loadmatch.platform.profiles.application.commandservices;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Carrier;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.EnableCarrierCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.RegisterCarrierCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.UpdateCarrierProfileCommand;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

/**
 * Carrier command service
 */
public interface CarrierCommandService {
    /**
     * Registers a carrier and its user account; the carrier stays pending of documentary validation
     * @param command the register carrier command
     * @return the registered carrier or an application error
     */
    Result<Carrier, ApplicationError> handle(RegisterCarrierCommand command);

    /**
     * Enables a carrier to accept trips
     * @param command the enable carrier command
     * @return the enabled carrier or an application error
     */
    Result<Carrier, ApplicationError> handle(EnableCarrierCommand command);

    /**
     * Updates the contact data of a carrier profile
     * @param command the update carrier profile command
     * @return the updated carrier or an application error
     */
    Result<Carrier, ApplicationError> handle(UpdateCarrierProfileCommand command);
}
