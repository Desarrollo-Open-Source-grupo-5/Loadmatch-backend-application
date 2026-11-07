package com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.fleet.application.commandservices.VehicleCommandService;
import com.cargolinklabs.loadmatch.platform.fleet.application.queryservices.VehicleQueryService;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.commands.DeactivateVehicleCommand;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries.GetVehicleByIdQuery;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries.GetVehiclesByCarrierIdQuery;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleId;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources.RegisterVehicleResource;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources.UpdateVehicleResource;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources.VehicleResource;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.transform.RegisterVehicleCommandFromResourceAssembler;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.transform.UpdateVehicleCommandFromResourceAssembler;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.transform.VehicleResourceFromEntityAssembler;
import com.cargolinklabs.loadmatch.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for the vehicles of the carrier fleet
 */
@RestController
@RequestMapping(value = "/api/v1/vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vehicles", description = "Carrier Fleet Management Endpoints")
public class VehiclesController {
    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;

    public VehiclesController(VehicleCommandService vehicleCommandService, VehicleQueryService vehicleQueryService) {
        this.vehicleCommandService = vehicleCommandService;
        this.vehicleQueryService = vehicleQueryService;
    }

    @PostMapping
    @Operation(summary = "Register a vehicle", description = "Adds a vehicle to the carrier fleet, pending of documentary validation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle registered"),
            @ApiResponse(responseCode = "400", description = "Invalid license plate or vehicle data"),
            @ApiResponse(responseCode = "404", description = "Carrier or vehicle type not found"),
            @ApiResponse(responseCode = "409", description = "License plate already registered")
    })
    public ResponseEntity<?> registerVehicle(@Valid @RequestBody RegisterVehicleResource resource) {
        var result = vehicleCommandService.handle(RegisterVehicleCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                VehicleResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get the vehicles of a carrier")
    public ResponseEntity<List<VehicleResource>> getVehiclesByCarrierId(@RequestParam UUID carrierId) {
        return ResponseEntity.ok(vehicleQueryService.handle(new GetVehiclesByCarrierIdQuery(new CarrierId(carrierId))).stream()
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }

    @GetMapping("/{vehicleId}")
    @Operation(summary = "Get vehicle by id")
    public ResponseEntity<VehicleResource> getVehicleById(@PathVariable UUID vehicleId) {
        return vehicleQueryService.handle(new GetVehicleByIdQuery(new VehicleId(vehicleId)))
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{vehicleId}")
    @Operation(summary = "Update a vehicle", description = "Updates the technical data; the license plate is not editable")
    public ResponseEntity<?> updateVehicle(@PathVariable UUID vehicleId, @Valid @RequestBody UpdateVehicleResource resource) {
        var result = vehicleCommandService.handle(
                UpdateVehicleCommandFromResourceAssembler.toCommandFromResource(vehicleId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                VehicleResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }

    @PostMapping("/{vehicleId}/deactivation")
    @Operation(summary = "Deactivate a vehicle", description = "The vehicle is no longer considered in the freight search")
    public ResponseEntity<?> deactivateVehicle(@PathVariable UUID vehicleId) {
        var result = vehicleCommandService.handle(new DeactivateVehicleCommand(new VehicleId(vehicleId)));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                VehicleResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }
}
