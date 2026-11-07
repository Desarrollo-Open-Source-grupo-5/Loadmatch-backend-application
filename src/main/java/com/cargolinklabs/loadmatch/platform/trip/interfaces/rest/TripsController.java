package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.cargolinklabs.loadmatch.platform.trip.application.commandservices.TripCommandService;
import com.cargolinklabs.loadmatch.platform.trip.application.queryservices.TripQueryService;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.ConfirmDeliveryCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.ObserveDeliveryCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripByIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripsByCarrierIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripsByShipperIdQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.AcceptLoadRequestResource;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.ObserveDeliveryResource;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.TripResource;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.UpdateTripStatusResource;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.transform.AcceptLoadRequestCommandFromResourceAssembler;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.transform.TripResourceFromEntityAssembler;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.transform.UpdateTripStatusCommandFromResourceAssembler;
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
 * REST controller for trips
 */
@RestController
@RequestMapping(value = "/api/v1/trips", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trips", description = "Trip Execution Endpoints")
public class TripsController {
    private final TripCommandService tripCommandService;
    private final TripQueryService tripQueryService;

    public TripsController(TripCommandService tripCommandService, TripQueryService tripQueryService) {
        this.tripCommandService = tripCommandService;
        this.tripQueryService = tripQueryService;
    }

    @PostMapping
    @Operation(summary = "Accept a load request", description = "An enabled carrier accepts a published load request and the trip is created")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trip created"),
            @ApiResponse(responseCode = "409", description = "Load request no longer available"),
            @ApiResponse(responseCode = "422", description = "Carrier not enabled to accept trips")
    })
    public ResponseEntity<?> acceptLoadRequest(@Valid @RequestBody AcceptLoadRequestResource resource) {
        var result = tripCommandService.handle(AcceptLoadRequestCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                TripResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    @GetMapping("/{tripId}")
    @Operation(summary = "Get trip by id")
    public ResponseEntity<TripResource> getTripById(@PathVariable UUID tripId) {
        return tripQueryService.handle(new GetTripByIdQuery(new TripId(tripId)))
                .map(TripResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get trips by carrier or shipper", description = "Send carrierId or shipperId")
    public ResponseEntity<List<TripResource>> getTrips(@RequestParam(required = false) UUID carrierId,
                                                       @RequestParam(required = false) UUID shipperId) {
        if (carrierId == null && shipperId == null) {
            return ResponseEntity.badRequest().build();
        }
        var trips = carrierId != null
                ? tripQueryService.handle(new GetTripsByCarrierIdQuery(new CarrierId(carrierId)))
                : tripQueryService.handle(new GetTripsByShipperIdQuery(new ShipperId(shipperId)));
        return ResponseEntity.ok(trips.stream().map(TripResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @PostMapping("/{tripId}/status-updates")
    @Operation(summary = "Update trip status",
            description = "The carrier reports the progress of the trip. DELIVERED requires the evidence URL and asks the shipper to confirm the receipt.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip status updated"),
            @ApiResponse(responseCode = "400", description = "Invalid status or missing delivery evidence"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "422", description = "Transition not allowed from the current status")
    })
    public ResponseEntity<?> updateTripStatus(@PathVariable UUID tripId,
                                              @Valid @RequestBody UpdateTripStatusResource resource) {
        var result = tripCommandService.handle(
                UpdateTripStatusCommandFromResourceAssembler.toCommandFromResource(tripId, resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                TripResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }

    @PostMapping("/{tripId}/delivery-confirmation")
    @Operation(summary = "Confirm delivery", description = "The shipper confirms the cargo was received as agreed and the trip is completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip completed"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "422", description = "The trip has not been delivered")
    })
    public ResponseEntity<?> confirmDelivery(@PathVariable UUID tripId) {
        var result = tripCommandService.handle(new ConfirmDeliveryCommand(new TripId(tripId)));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                TripResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }

    @PostMapping("/{tripId}/delivery-observations")
    @Operation(summary = "Observe delivery", description = "The shipper registers an observation and the trip moves to DISPUTED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery observed"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "422", description = "The trip has not been delivered")
    })
    public ResponseEntity<?> observeDelivery(@PathVariable UUID tripId,
                                             @Valid @RequestBody ObserveDeliveryResource resource) {
        var result = tripCommandService.handle(new ObserveDeliveryCommand(new TripId(tripId), resource.observation()));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                TripResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }
}
