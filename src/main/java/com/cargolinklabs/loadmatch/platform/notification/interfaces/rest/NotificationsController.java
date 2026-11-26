package com.cargolinklabs.loadmatch.platform.notification.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.notification.application.commandservices.NotificationCommandService;
import com.cargolinklabs.loadmatch.platform.notification.application.queryservices.NotificationQueryService;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.commands.MarkNotificationsAsReadCommand;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.queries.CountUnreadNotificationsQuery;
import com.cargolinklabs.loadmatch.platform.notification.domain.model.queries.GetNotificationsByUserIdQuery;
import com.cargolinklabs.loadmatch.platform.notification.interfaces.rest.resources.UnreadNotificationCountResource;
import com.cargolinklabs.loadmatch.platform.notification.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import com.cargolinklabs.loadmatch.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.cargolinklabs.loadmatch.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "In-app notification endpoints")
public class NotificationsController {
    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    public NotificationsController(NotificationCommandService notificationCommandService,
                                   NotificationQueryService notificationQueryService) {
        this.notificationCommandService = notificationCommandService;
        this.notificationQueryService = notificationQueryService;
    }

    @GetMapping
    @Operation(summary = "List the authenticated user's notifications")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notifications listed"),
            @ApiResponse(responseCode = "400", description = "Invalid user identifier"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Notifications belong to another user")
    })
    public ResponseEntity<?> getNotifications(@RequestParam("userId") UUID userId, Principal principal) {
        var result = notificationQueryService.handle(new GetNotificationsByUserIdQuery(userId, principal.getName()));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                notifications -> notifications.stream().map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                        .toList(), HttpStatus.OK);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Count the authenticated user's unread notifications")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unread count returned"),
            @ApiResponse(responseCode = "400", description = "Invalid user identifier"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Notifications belong to another user")
    })
    public ResponseEntity<?> getUnreadCount(@RequestParam("userId") UUID userId, Principal principal) {
        var result = notificationQueryService.handle(new CountUnreadNotificationsQuery(userId, principal.getName()));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, UnreadNotificationCountResource::new,
                HttpStatus.OK);
    }

    @PostMapping("/read")
    @Operation(summary = "Mark all of the authenticated user's notifications as read")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notifications marked as read"),
            @ApiResponse(responseCode = "400", description = "Invalid user identifier"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Notifications belong to another user")
    })
    public ResponseEntity<?> markAllAsRead(@RequestParam("userId") UUID userId, Principal principal) {
        var result = notificationCommandService.handle(new MarkNotificationsAsReadCommand(userId, principal.getName()));
        return switch (result) {
            case Result.Success<Integer, ApplicationError> ignored -> ResponseEntity.noContent().build();
            case Result.Failure<Integer, ApplicationError> failure ->
                    ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(failure.error());
        };
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleInvalidUserId() {
        return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                ApplicationError.validationError("userId", ""));
    }
}
