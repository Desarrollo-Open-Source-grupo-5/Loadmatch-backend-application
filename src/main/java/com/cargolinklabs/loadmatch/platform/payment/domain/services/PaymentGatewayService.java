package com.cargolinklabs.loadmatch.platform.payment.domain.services;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.Money;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.PaymentResult;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.PaymentToken;

/**
 * Payment gateway port used by the payment to charge the shipper
 */
public interface PaymentGatewayService {
    /**
     * Executes the charge of the approved instrument
     * @param token the gateway token of the approved instrument
     * @param amount the amount to charge
     * @return the result of the charge, approved with its gateway reference or rejected with its error code
     */
    PaymentResult executeCharge(PaymentToken token, Money amount);
}
