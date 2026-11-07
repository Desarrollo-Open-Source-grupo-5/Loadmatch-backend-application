package com.cargolinklabs.loadmatch.platform.fleet.application.commandservices;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.Vehicle;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.DeactivateVehicleCommand;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.RegisterVehicleCommand;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.UpdateVehicleCommand;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

/**
 * Vehicle command service
 */
public interface VehicleCommandService {
    Result<Vehicle, ApplicationError> handle(RegisterVehicleCommand command);
    Result<Vehicle, ApplicationError> handle(UpdateVehicleCommand command);
    Result<Vehicle, ApplicationError> handle(DeactivateVehicleCommand command);
}
