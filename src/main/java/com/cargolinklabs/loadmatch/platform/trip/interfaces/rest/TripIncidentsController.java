package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import com.cargolinklabs.loadmatch.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.cargolinklabs.loadmatch.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.cargolinklabs.loadmatch.platform.trip.application.commandservices.TripCommandService;
import com.cargolinklabs.loadmatch.platform.trip.application.queryservices.TripQueryService;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetIncidentEvidenceQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.queries.GetTripIncidentsQuery;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.IncidentEvidence;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.ReportIncidentResource;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.transform.IncidentResourceFromEntityAssembler;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.transform.ReportIncidentCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trips/{tripId}/incidents")
@Tag(name = "Trips", description = "Trip Execution Endpoints")
public class TripIncidentsController {
    private final TripCommandService tripCommandService;
    private final TripQueryService tripQueryService;

    public TripIncidentsController(TripCommandService tripCommandService, TripQueryService tripQueryService) {
        this.tripCommandService = tripCommandService;
        this.tripQueryService = tripQueryService;
    }

    @PostMapping
    @Operation(summary = "Report a trip incident", description = "The assigned carrier reports an incident with evidence")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Incident reported and trip delayed"),
            @ApiResponse(responseCode = "400", description = "Invalid incident or evidence"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Carrier role required"),
            @ApiResponse(responseCode = "404", description = "Trip not found for the caller"),
            @ApiResponse(responseCode = "422", description = "Trip cannot be delayed in its current state")
    })
    public ResponseEntity<?> reportIncident(@PathVariable UUID tripId, Principal principal,
                                            @Valid @RequestBody ReportIncidentResource resource) {
        var result = tripCommandService.handle(
                ReportIncidentCommandFromResourceAssembler.toCommandFromResource(tripId, principal.getName(), resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                IncidentResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List trip incidents", description = "Returns incident details without the evidence bytes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Incidents listed"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Trip not found for the caller")
    })
    public ResponseEntity<?> getIncidents(@PathVariable UUID tripId, Principal principal) {
        var result = tripQueryService.handle(new GetTripIncidentsQuery(new TripId(tripId), principal.getName()));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                incidents -> incidents.stream().map(IncidentResourceFromEntityAssembler::toResourceFromEntity).toList(),
                HttpStatus.OK);
    }

    @GetMapping("/{incidentId}/evidence")
    @Operation(summary = "Download the evidence of a trip incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evidence file"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Trip or incident evidence not found for the caller")
    })
    public ResponseEntity<?> downloadEvidence(@PathVariable UUID tripId, @PathVariable UUID incidentId,
                                              Principal principal) {
        var result = tripQueryService.handle(new GetIncidentEvidenceQuery(new TripId(tripId), incidentId,
                principal.getName()));
        return switch (result) {
            case Result.Success<IncidentEvidence, ApplicationError> success -> evidenceResponse(success.value());
            case Result.Failure<IncidentEvidence, ApplicationError> failure ->
                    ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(failure.error());
        };
    }

    private ResponseEntity<byte[]> evidenceResponse(IncidentEvidence evidence) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(evidence.contentType()))
                .cacheControl(CacheControl.noStore())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(evidence.fileName(), StandardCharsets.UTF_8)
                                .build().toString())
                .header("X-Content-Type-Options", "nosniff")
                .body(evidence.content());
    }
}
