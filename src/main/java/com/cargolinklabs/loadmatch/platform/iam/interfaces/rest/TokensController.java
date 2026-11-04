package com.cargolinklabs.loadmatch.platform.iam.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.iam.application.commandservices.UserCommandService;
import com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.resources.SignInResource;
import com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.cargolinklabs.loadmatch.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication tokens (sign in)
 */
@RestController
@RequestMapping(value = "/api/v1/tokens", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tokens", description = "Authentication Endpoints")
public class TokensController {
    private final UserCommandService userCommandService;

    public TokensController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping
    @Operation(summary = "Sign in", description = "Authenticates a shipper or carrier with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated"),
            @ApiResponse(responseCode = "400", description = "Invalid email or password")
    })
    public ResponseEntity<?> generateToken(@Valid @RequestBody SignInResource resource) {
        var result = userCommandService.handle(SignInCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                AuthenticatedUserResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.OK);
    }
}
