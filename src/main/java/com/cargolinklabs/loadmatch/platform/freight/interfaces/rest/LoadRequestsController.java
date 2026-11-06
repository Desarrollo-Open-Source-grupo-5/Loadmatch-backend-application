package com.cargolinklabs.loadmatch.platform.freight.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.freight.application.commandservices.LoadRequestCommandService;
import com.cargolinklabs.loadmatch.platform.freight.application.queryservices.LoadRequestQueryService;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.queries.GetLoadRequestByIdQuery;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.queries.GetLoadRequestsByShipperIdQuery;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.LoadRequestId;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.freight.interfaces.rest.resources.CreateLoadRequestResource;
import com.cargolinklabs.loadmatch.platform.freight.interfaces.rest.resources.LoadRequestResource;
import com.cargolinklabs.loadmatch.platform.freight.interfaces.rest.transform.CreateLoadRequestCommandFromResourceAssembler;
import com.cargolinklabs.loadmatch.platform.freight.interfaces.rest.transform.LoadRequestResourceFromEntityAssembler;
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
 * REST controller for load requests
 */
@RestController
@RequestMapping(value = "/api/v1/load-requests", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Load Requests", description = "Load Request Publishing Endpoints")
public class LoadRequestsController {
    private final LoadRequestCommandService loadRequestCommandService;
    private final LoadRequestQueryService loadRequestQueryService;

    public LoadRequestsController(LoadRequestCommandService loadRequestCommandService,
                                  LoadRequestQueryService loadRequestQueryService) {
        this.loadRequestCommandService = loadRequestCommandService;
        this.loadRequestQueryService = loadRequestQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a load request", description = "Registers and publishes a load request of a shipper")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Load request created and published"),
            @ApiResponse(responseCode = "400", description = "Incomplete or invalid payload")
    })
    public ResponseEntity<?> createLoadRequest(@Valid @RequestBody CreateLoadRequestResource resource) {
        var result = loadRequestCommandService.handle(
                CreateLoadRequestCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                LoadRequestResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    @GetMapping("/{loadRequestId}")
    @Operation(summary = "Get load request by id")
    public ResponseEntity<LoadRequestResource> getLoadRequestById(@PathVariable UUID loadRequestId) {
        return loadRequestQueryService.handle(new GetLoadRequestByIdQuery(new LoadRequestId(loadRequestId)))
                .map(LoadRequestResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get load requests of a shipper", description = "Returns the load requests of a shipper, newest first")
    public ResponseEntity<List<LoadRequestResource>> getLoadRequestsByShipperId(@RequestParam UUID shipperId) {
        var loadRequests = loadRequestQueryService.handle(new GetLoadRequestsByShipperIdQuery(new ShipperId(shipperId)));
        return ResponseEntity.ok(loadRequests.stream()
                .map(LoadRequestResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }
}
