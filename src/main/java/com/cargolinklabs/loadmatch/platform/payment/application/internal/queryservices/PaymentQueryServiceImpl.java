package com.cargolinklabs.loadmatch.platform.payment.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.payment.application.internal.outboundservices.acl.ExternalProfilesService;
import com.cargolinklabs.loadmatch.platform.payment.application.queryservices.PaymentQueryService;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates.Payment;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.queries.GetPaymentByTripIdQuery;
import com.cargolinklabs.loadmatch.platform.payment.domain.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Payment query service implementation
 */
@Service
public class PaymentQueryServiceImpl implements PaymentQueryService {
    private final PaymentRepository paymentRepository;
    private final ExternalProfilesService externalProfilesService;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository,
                                   ExternalProfilesService externalProfilesService) {
        this.paymentRepository = paymentRepository;
        this.externalProfilesService = externalProfilesService;
    }

    @Override
    public Optional<Payment> handle(GetPaymentByTripIdQuery query) {
        return paymentRepository.findByTripId(query.tripId())
                .filter(payment -> externalProfilesService.isShipper(payment.getShipperId().shipperId(),
                        query.requesterEmail())
                        || externalProfilesService.isCarrier(payment.getCarrierId().carrierId(),
                        query.requesterEmail()));
    }
}
