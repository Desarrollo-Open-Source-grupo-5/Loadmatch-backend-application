package com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects;

import java.util.UUID;

/**
 * Carrier identifier value object referenced by the payment
 *
 * @param carrierId the UUID identifier
 */
public record CarrierId(UUID carrierId) {
    public CarrierId {
        if (carrierId == null) {
            throw new IllegalArgumentException("CarrierId cannot be null");
        }
    }
}
