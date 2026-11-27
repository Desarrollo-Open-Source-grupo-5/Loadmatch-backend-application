package com.cargolinklabs.loadmatch.platform.payment.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.commands.CreatePaymentCommand;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.PaymentMethod;
import com.cargolinklabs.loadmatch.platform.payment.interfaces.rest.resources.CreatePaymentResource;

/**
 * Assembler that translates a payment resource into a create payment command
 */
public final class CreatePaymentCommandFromResourceAssembler {

    private CreatePaymentCommandFromResourceAssembler() {
    }

    public static CreatePaymentCommand toCommandFromResource(CreatePaymentResource resource, String payerEmail) {
        return new CreatePaymentCommand(resource.tripId(), resource.totalAmount(),
                PaymentMethod.valueOf(resource.method()), resource.paymentToken(), payerEmail);
    }
}
