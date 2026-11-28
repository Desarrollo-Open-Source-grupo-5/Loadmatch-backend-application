package com.cargolinklabs.loadmatch.platform.rating.application.internal.outboundservices.acl;

import com.cargolinklabs.loadmatch.platform.payment.interfaces.acl.PaymentContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Outbound ACL service wrapping PaymentContextFacade
 */
@Service
public class ExternalPaymentService {
    private final PaymentContextFacade paymentContextFacade;

    public ExternalPaymentService(PaymentContextFacade paymentContextFacade) {
        this.paymentContextFacade = paymentContextFacade;
    }

    public boolean isTripPaid(UUID tripId) {
        return paymentContextFacade.isTripPaid(tripId);
    }
}
