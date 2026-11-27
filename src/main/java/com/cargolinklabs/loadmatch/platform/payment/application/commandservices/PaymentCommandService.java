package com.cargolinklabs.loadmatch.platform.payment.application.commandservices;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.aggregates.Payment;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.commands.CreatePaymentCommand;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

/**
 * Payment command service
 */
public interface PaymentCommandService {
    /**
     * Charges a completed trip to its shipper through the payment gateway
     * @param command the create payment command
     * @return the completed or rejected payment, or an application error
     */
    Result<Payment, ApplicationError> handle(CreatePaymentCommand command);
}
