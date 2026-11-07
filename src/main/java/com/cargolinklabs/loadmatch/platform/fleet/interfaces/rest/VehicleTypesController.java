package com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.fleet.application.queryservices.VehicleTypeQueryService;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.queries.GetAllVehicleTypesQuery;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.resources.VehicleTypeResource;
import com.cargolinklabs.loadmatch.platform.fleet.interfaces.rest.transform.VehicleTypeResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for the vehicle type catalog
 */
@RestController
@RequestMapping(value = "/api/v1/vehicle-types", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Vehicle Types", description = "Vehicle Type Catalog Endpoints")
public class VehicleTypesController {
    private final VehicleTypeQueryService vehicleTypeQueryService;

    public VehicleTypesController(VehicleTypeQueryService vehicleTypeQueryService) {
        this.vehicleTypeQueryService = vehicleTypeQueryService;
    }

    @GetMapping
    @Operation(summary = "Get all vehicle types")
    public ResponseEntity<List<VehicleTypeResource>> getAllVehicleTypes() {
        return ResponseEntity.ok(vehicleTypeQueryService.handle(new GetAllVehicleTypesQuery()).stream()
                .map(VehicleTypeResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }
}
