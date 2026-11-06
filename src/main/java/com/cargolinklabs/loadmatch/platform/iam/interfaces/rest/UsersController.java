package com.cargolinklabs.loadmatch.platform.iam.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.iam.application.queryservices.UserQueryService;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.UserId;
import com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.resources.UserResource;
import com.cargolinklabs.loadmatch.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for users
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User Management Endpoints")
public class UsersController {
    private final UserQueryService userQueryService;

    public UsersController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by id")
    public ResponseEntity<UserResource> getUserById(@PathVariable UUID userId) {
        return userQueryService.handle(new GetUserByIdQuery(new UserId(userId)))
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
