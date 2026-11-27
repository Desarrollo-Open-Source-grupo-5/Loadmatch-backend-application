package com.cargolinklabs.loadmatch.platform.payment.application.acl;

import com.cargolinklabs.loadmatch.platform.payment.interfaces.acl.PaymentContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Payment context facade implementation
 * <p>
 *     Answers that no trip is paid until the payment repository is introduced with the PayPal charge (T49 (US20)),
 *     so Rating can integrate against the agreed contract in the meantime.
 * </p>
 */
@Service
public class PaymentContextFacadeImpl implements PaymentContextFacade {

    @Override
    public boolean isTripPaid(UUID tripId) {
        return false;
    }
}
