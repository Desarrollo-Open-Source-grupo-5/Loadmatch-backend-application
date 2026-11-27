package com.cargolinklabs.loadmatch.platform.payment.application.queryservices;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates.Payment;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.queries.GetPaymentByTripIdQuery;

import java.util.Optional;

/**
 * Payment query service
 */
public interface PaymentQueryService {
    Optional<Payment> handle(GetPaymentByTripIdQuery query);
}
