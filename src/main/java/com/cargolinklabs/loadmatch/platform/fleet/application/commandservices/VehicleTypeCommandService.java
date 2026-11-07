package com.cargolinklabs.loadmatch.platform.fleet.application.commandservices;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.SeedVehicleTypesCommand;

/**
 * Vehicle type command service
 */
public interface VehicleTypeCommandService {
    void handle(SeedVehicleTypesCommand command);
}
