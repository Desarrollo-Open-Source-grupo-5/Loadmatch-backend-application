package com.cargolinklabs.loadmatch.platform.rating.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.rating.application.commandservices.RatingCommandService;
import com.cargolinklabs.loadmatch.platform.rating.application.queryservices.RatingQueryService;
import com.cargolinklabs.loadmatch.platform.rating.domain.model.queries.GetRatingsByRateeIdQuery;
import com.cargolinklabs.loadmatch.platform.rating.interfaces.rest.resources.RatingResource;
import com.cargolinklabs.loadmatch.platform.rating.interfaces.rest.resources.RegisterRatingResource;
import com.cargolinklabs.loadmatch.platform.rating.interfaces.rest.transform.RatingResourceFromEntityAssembler;
import com.cargolinklabs.loadmatch.platform.rating.interfaces.rest.transform.RegisterRatingCommandFromResourceAssembler;
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
 * REST controller for Rating endpoints
 */
@RestController
@RequestMapping(value = "/api/v1/ratings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Ratings", description = "Rating and reputation endpoints")
public class RatingsController {
    private final RatingCommandService ratingCommandService;
    private final RatingQueryService ratingQueryService;

    public RatingsController(RatingCommandService ratingCommandService,
                             RatingQueryService ratingQueryService) {
        this.ratingCommandService = ratingCommandService;
        this.ratingQueryService = ratingQueryService;
    }

    @PostMapping
    @Operation(summary = "Register a rating for a completed trip")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rating created"),
            @ApiResponse(responseCode = "400", description = "Invalid rating payload"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "User is not a participant of the trip"),
            @ApiResponse(responseCode = "404", description = "Trip not found"),
            @ApiResponse(responseCode = "409", description = "Trip already rated by this user")
    })
    public ResponseEntity<?> registerRating(@Valid @RequestBody RegisterRatingResource resource) {
        var command = RegisterRatingCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = ratingCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                RatingResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "List ratings received by a profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ratings listed"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<List<RatingResource>> getRatingsByRatee(@RequestParam("rateeId") UUID rateeId) {
        var ratings = ratingQueryService.handle(new GetRatingsByRateeIdQuery(rateeId)).stream()
                .map(RatingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(ratings);
    }
}

