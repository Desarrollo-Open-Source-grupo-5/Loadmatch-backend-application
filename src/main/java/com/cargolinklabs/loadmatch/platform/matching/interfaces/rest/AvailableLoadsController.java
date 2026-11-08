package com.cargolinklabs.loadmatch.platform.matching.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.matching.application.queryservices.AvailableLoadQueryService;
import com.cargolinklabs.loadmatch.platform.matching.domain.model.queries.GetAvailableLoadsNearbyQuery;
import com.cargolinklabs.loadmatch.platform.matching.domain.model.valueobjects.SearchCriteria;
import com.cargolinklabs.loadmatch.platform.matching.interfaces.rest.resources.AvailableLoadResource;
import com.cargolinklabs.loadmatch.platform.matching.interfaces.rest.transform.AvailableLoadResourceFromReadModelAssembler;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.valueobjects.Coordinates;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for the geospatial matching of available loads
 */
@RestController
@RequestMapping(value = "/api/v1/available-loads", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Available Loads", description = "Geospatial Matching Endpoints")
public class AvailableLoadsController {
    private final AvailableLoadQueryService availableLoadQueryService;

    public AvailableLoadsController(AvailableLoadQueryService availableLoadQueryService) {
        this.availableLoadQueryService = availableLoadQueryService;
    }

    @GetMapping
    @Operation(summary = "Get nearby available loads",
            description = "Returns the published loads whose pickup point is within the radius (Haversine distance), nearest first")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available loads returned"),
            @ApiResponse(responseCode = "400", description = "Invalid coordinates or radius")
    })
    public ResponseEntity<List<AvailableLoadResource>> getAvailableLoadsNearby(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "30") double radiusKm) {
        var criteria = new SearchCriteria(new Coordinates(latitude, longitude), radiusKm);
        var availableLoads = availableLoadQueryService.handle(new GetAvailableLoadsNearbyQuery(criteria));
        return ResponseEntity.ok(availableLoads.stream()
                .map(AvailableLoadResourceFromReadModelAssembler::toResourceFromReadModel)
                .toList());
    }
}
