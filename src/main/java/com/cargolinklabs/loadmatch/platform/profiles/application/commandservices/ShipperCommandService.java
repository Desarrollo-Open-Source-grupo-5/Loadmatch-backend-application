package com.cargolinklabs.loadmatch.platform.profiles.application.commandservices;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Shipper;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.RegisterShipperCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.UpdateShipperProfileCommand;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

/**
 * Shipper command service
 */
public interface ShipperCommandService {
    /**
     * Registers a shipper and its user account
     * @param command the register shipper command
     * @return the registered shipper or an application error
     */
    Result<Shipper, ApplicationError> handle(RegisterShipperCommand command);

    /**
     * Updates the editable data of a shipper profile; the RUC and the business name are not editable
     * @param command the update shipper profile command
     * @return the updated shipper or an application error
     */
    Result<Shipper, ApplicationError> handle(UpdateShipperProfileCommand command);
}
