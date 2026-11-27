package com.cargolinklabs.loadmatch.platform.payment.application.acl;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates.Payment;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.payment.domain.repositories.PaymentRepository;
import com.cargolinklabs.loadmatch.platform.payment.interfaces.acl.PaymentContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Payment context facade implementation that delegates to the payment repository
 */
@Service
public class PaymentContextFacadeImpl implements PaymentContextFacade {
    private final PaymentRepository paymentRepository;

    public PaymentContextFacadeImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public boolean isTripPaid(UUID tripId) {
        if (tripId == null) {
            return false;
        }
        return paymentRepository.findByTripId(new TripId(tripId)).map(Payment::isCompleted).orElse(false);
    }
}
