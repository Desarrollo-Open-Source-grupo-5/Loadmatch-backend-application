package com.cargolinklabs.loadmatch.platform.payment.domain.repositories;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates.Payment;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.PaymentId;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.TripId;

import java.util.Optional;

/**
 * Payment repository port
 */
public interface PaymentRepository {
    Optional<Payment> findById(PaymentId id);

    Optional<Payment> findByTripId(TripId tripId);

    Payment save(Payment payment);
}
