package com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects;

/**
 * Result returned by the payment gateway after executing a charge
 *
 * @param successful true when the gateway confirmed the charge
 * @param reference the gateway reference, present only when the charge succeeded
 * @param errorCode the gateway error code, present only when the charge was rejected
 */
public record PaymentResult(boolean successful, GatewayReference reference, String errorCode) {
    public PaymentResult {
        if (successful && reference == null) {
            throw new IllegalArgumentException("A successful charge requires the gateway reference");
        }
        if (!successful && (errorCode == null || errorCode.isBlank())) {
            throw new IllegalArgumentException("A rejected charge requires the gateway error code");
        }
    }

    public static PaymentResult approved(GatewayReference reference) {
        return new PaymentResult(true, reference, null);
    }

    public static PaymentResult rejected(String errorCode) {
        return new PaymentResult(false, null, errorCode);
    }
}
