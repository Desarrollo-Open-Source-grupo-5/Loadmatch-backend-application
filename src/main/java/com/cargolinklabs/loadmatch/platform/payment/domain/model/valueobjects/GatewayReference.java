package com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects;

import java.time.LocalDateTime;

/**
 * Reference of the transaction confirmed by the payment gateway
 *
 * @param value the transaction identifier assigned by the gateway
 * @param processedAt the moment the gateway processed the transaction
 */
public record GatewayReference(String value, LocalDateTime processedAt) {
    public GatewayReference {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Gateway reference cannot be blank");
        }
        if (processedAt == null) {
            throw new IllegalArgumentException("Processing date cannot be null");
        }
    }
}
