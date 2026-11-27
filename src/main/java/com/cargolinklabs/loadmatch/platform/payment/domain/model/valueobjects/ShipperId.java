package com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects;

import java.util.UUID;

/**
 * Shipper identifier value object referenced by the payment
 *
 * @param shipperId the UUID identifier
 */
public record ShipperId(UUID shipperId) {
    public ShipperId {
        if (shipperId == null) {
            throw new IllegalArgumentException("ShipperId cannot be null");
        }
    }
}
