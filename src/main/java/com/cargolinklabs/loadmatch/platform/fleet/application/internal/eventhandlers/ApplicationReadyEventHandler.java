package com.cargolinklabs.loadmatch.platform.fleet.application.internal.eventhandlers;

import com.cargolinklabs.loadmatch.platform.fleet.application.commandservices.VehicleTypeCommandService;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.SeedVehicleTypesCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Seeds the catalog of vehicle types when the application is ready
 */
@Service("fleetApplicationReadyEventHandler")
public class ApplicationReadyEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);
    private final VehicleTypeCommandService vehicleTypeCommandService;

    public ApplicationReadyEventHandler(VehicleTypeCommandService vehicleTypeCommandService) {
        this.vehicleTypeCommandService = vehicleTypeCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        LOGGER.info("Verifying if vehicle types seeding is needed");
        vehicleTypeCommandService.handle(new SeedVehicleTypesCommand());
        LOGGER.info("Vehicle types seeding verified");
    }
}
