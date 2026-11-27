package com.cargolinklabs.loadmatch.platform.payment.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.payment.application.commandservices.PaymentCommandService;
import com.cargolinklabs.loadmatch.platform.payment.application.queryservices.PaymentQueryService;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.queries.GetPaymentByTripIdQuery;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.payment.interfaces.rest.resources.CreatePaymentResource;
import com.cargolinklabs.loadmatch.platform.payment.interfaces.rest.resources.PaymentResource;
import com.cargolinklabs.loadmatch.platform.payment.interfaces.rest.transform.CreatePaymentCommandFromResourceAssembler;
import com.cargolinklabs.loadmatch.platform.payment.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
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

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for payments
 */
@RestController
@RequestMapping(value = "/api/v1/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payments", description = "Payment Endpoints")
public class PaymentsController {
    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;

    public PaymentsController(PaymentCommandService paymentCommandService, PaymentQueryService paymentQueryService) {
        this.paymentCommandService = paymentCommandService;
        this.paymentQueryService = paymentQueryService;
    }

    @PostMapping
    @Operation(summary = "Pay a completed trip",
            description = "The shipper pays a completed trip with the order approved in PayPal sandbox. A rejected payment can be retried with another order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment registered as COMPLETED or REJECTED by the gateway"),
            @ApiResponse(responseCode = "400", description = "Invalid amount, method or payment token"),
            @ApiResponse(responseCode = "404", description = "Trip not found for the authenticated shipper"),
            @ApiResponse(responseCode = "409", description = "The trip is already paid or its payment is in progress"),
            @ApiResponse(responseCode = "422", description = "The trip is not completed")
    })
    public ResponseEntity<?> createPayment(@Valid @RequestBody CreatePaymentResource resource, Principal principal) {
        var result = paymentCommandService.handle(
                CreatePaymentCommandFromResourceAssembler.toCommandFromResource(resource, principal.getName()));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                PaymentResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get the payment of a trip", description = "Available to the shipper and the carrier of the trip")
    public ResponseEntity<List<PaymentResource>> getPaymentsByTripId(@RequestParam UUID tripId, Principal principal) {
        return ResponseEntity.ok(paymentQueryService.handle(new GetPaymentByTripIdQuery(new TripId(tripId),
                        principal.getName())).stream()
                .map(PaymentResourceFromEntityAssembler::toResourceFromEntity)
                .toList());
    }
}
