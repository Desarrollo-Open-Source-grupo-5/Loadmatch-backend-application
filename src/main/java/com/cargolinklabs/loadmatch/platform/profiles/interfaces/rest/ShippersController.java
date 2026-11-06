package com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.profiles.application.commandservices.ShipperCommandService;
import com.cargolinklabs.loadmatch.platform.profiles.application.queryservices.ShipperQueryService;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetShipperByIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.queries.GetShipperByUserIdQuery;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.UserId;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.RegisterShipperResource;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.ShipperResource;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.resources.UpdateShipperProfileResource;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform.RegisterShipperCommandFromResourceAssembler;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform.ShipperResourceFromEntityAssembler;
import com.cargolinklabs.loadmatch.platform.profiles.interfaces.rest.transform.UpdateShipperProfileCommandFromResourceAssembler;
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
 * REST controller for shippers (businesses that publish load requests)
 */
@RestController
@RequestMapping(value = "/api/v1/shippers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Shippers", description = "Shipper Registration and Profile Endpoints")
public class ShippersController {
    private final ShipperCommandService shipperCommandService;
    private final ShipperQueryService shipperQueryService;

    public ShippersController(ShipperCommandService shipperCommandService, ShipperQueryService shipperQueryService) {
        this.shipperCommandService = shipperCommandService;
        this.shipperQueryService = shipperQueryService;
    }

    @PostMapping
    @Operation(summary = "Register a shipper", description = "Creates the shipper profile together with its user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Shipper registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "RUC or email already registered")
    })
    public ResponseEntity<?> registerShipper(@Valid @RequestBody RegisterShipperResource resource) {
        var result = shipperCommandService.handle(RegisterShipperCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ShipperResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    @GetMapping("/{shipperId}")
    @Operation(summary = "Get shipper by id")
    public ResponseEntity<ShipperResource> getShipperById(@PathVariable UUID shipperId) {
        return shipperQueryService.handle(new GetShipperByIdQuery(new ShipperId(shipperId)))
                .map(ShipperResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get shipper by user id", description = "Returns the shipper profile that belongs to the signed in user")
    public ResponseEntity<ShipperResource> getShipperByUserId(@RequestParam UUID userId) {
        return shipperQueryService.handle(new GetShipperByUserIdQuery(new UserId(userId)))
                .map(ShipperResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{shipperId}")
    @Operation(summary = "Update shipper profile",
            description = "Updates the contact data and fiscal address. The RUC and the business name can only be corrected through support.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipper profile updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Shipper not found")
    })
    public ResponseEntity<?> updateShipperProfile(@PathVariable UUID shipperId,
                                                  @Valid @RequestBody UpdateShipperProfileResource resource) {
        var result = shipperCommandService.handle(
                UpdateShipperProfileCommandFromResourceAssembler.toCommandFromResource(shipperId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ShipperResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }
}
