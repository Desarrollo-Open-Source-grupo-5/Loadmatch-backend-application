package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.profiles.application.commandservices.CarrierCommandService;
import com.cargolinklabs.loadmatch.platform.profiles.application.queryservices.CarrierQueryService;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.commands.EnableCarrierCommand;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetCarrierByIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.CarrierResource;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.RegisterCarrierResource;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform.CarrierResourceFromEntityAssembler;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform.RegisterCarrierCommandFromResourceAssembler;
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

import java.util.UUID;

/**
 * REST controller for carriers
 */
@RestController
@RequestMapping(value = "/api/v1/carriers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Carriers", description = "Carrier Registration and Profile Endpoints")
public class CarriersController {
    private final CarrierCommandService carrierCommandService;
    private final CarrierQueryService carrierQueryService;

    public CarriersController(CarrierCommandService carrierCommandService, CarrierQueryService carrierQueryService) {
        this.carrierCommandService = carrierCommandService;
        this.carrierQueryService = carrierQueryService;
    }

    @PostMapping
    @Operation(summary = "Register a carrier",
            description = "Creates the carrier profile together with its user account. The carrier stays pending of documentary validation. Its vehicles are registered through the vehicles endpoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Carrier registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "DNI or email already registered")
    })
    public ResponseEntity<?> registerCarrier(@Valid @RequestBody RegisterCarrierResource resource) {
        var result = carrierCommandService.handle(RegisterCarrierCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                CarrierResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    @GetMapping("/{carrierId}")
    @Operation(summary = "Get carrier by id")
    public ResponseEntity<CarrierResource> getCarrierById(@PathVariable UUID carrierId) {
        return carrierQueryService.handle(new GetCarrierByIdQuery(new CarrierId(carrierId)))
                .map(CarrierResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{carrierId}/enablement")
    @Operation(summary = "Enable a carrier",
            description = "Enables the carrier to accept trips. Temporary endpoint until the Document Validation bounded context is implemented.")
    public ResponseEntity<?> enableCarrier(@PathVariable UUID carrierId) {
        var result = carrierCommandService.handle(new EnableCarrierCommand(new CarrierId(carrierId)));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                CarrierResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }
}
