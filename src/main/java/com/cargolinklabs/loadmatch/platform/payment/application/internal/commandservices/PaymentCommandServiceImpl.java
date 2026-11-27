package com.cargolinklabs.loadmatch.platform.payment.application.internal.commandservices;

import com.cargolinklabs.loadmatch.platform.payment.application.commandservices.PaymentCommandService;
import com.cargolinklabs.loadmatch.platform.payment.application.internal.outboundservices.acl.ExternalProfilesService;
import com.cargolinklabs.loadmatch.platform.payment.application.internal.outboundservices.acl.ExternalTripService;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates.Payment;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.commands.CreatePaymentCommand;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.payment.domain.repositories.PaymentRepository;
import com.cargolinklabs.loadmatch.platform.payment.domain.services.PaymentGatewayService;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Payment command service implementation; the platform fee rate comes from PAYMENT_PLATFORM_FEE_RATE (10% by default)
 */
@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {
    private static final String COMPLETED_TRIP_STATUS = "COMPLETED";

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayService paymentGatewayService;
    private final ExternalTripService externalTripService;
    private final ExternalProfilesService externalProfilesService;
    private final BigDecimal platformFeeRate;

    public PaymentCommandServiceImpl(PaymentRepository paymentRepository, PaymentGatewayService paymentGatewayService,
                                     ExternalTripService externalTripService,
                                     ExternalProfilesService externalProfilesService,
                                     @Value("${payment.platform-fee-rate:0.10}") BigDecimal platformFeeRate) {
        this.paymentRepository = paymentRepository;
        this.paymentGatewayService = paymentGatewayService;
        this.externalTripService = externalTripService;
        this.externalProfilesService = externalProfilesService;
        this.platformFeeRate = platformFeeRate;
    }

    @Override
    public Result<Payment, ApplicationError> handle(CreatePaymentCommand command) {
        var tripId = new TripId(command.tripId());
        var trip = externalTripService.fetchTripById(tripId)
                .filter(snapshot -> externalProfilesService.isShipper(snapshot.shipperId(), command.payerEmail()));
        if (trip.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Trip", command.tripId().toString()));
        }
        if (!COMPLETED_TRIP_STATUS.equals(trip.get().status())) {
            return Result.failure(ApplicationError.businessRuleViolation("trip-must-be-completed",
                    "Only a completed trip can be paid, current status is %s".formatted(trip.get().status())));
        }
        var payment = new Payment(tripId, new ShipperId(trip.get().shipperId()),
                new CarrierId(trip.get().carrierId()), Money.ofSoles(command.totalAmount()), platformFeeRate);
        return Result.success(paymentRepository.save(charge(payment, command)));
    }

    private Payment charge(Payment payment, CreatePaymentCommand command) {
        var token = new PaymentToken(command.paymentToken());
        payment.process(command.method(), token);
        var result = paymentGatewayService.executeCharge(token, payment.getTotalAmount());
        if (result.successful()) {
            payment.confirm(result.reference());
        } else {
            payment.reject(result.errorCode());
        }
        return payment;
    }
}
