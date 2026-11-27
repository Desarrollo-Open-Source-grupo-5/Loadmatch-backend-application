package com.cargolinklabs.loadmatch.platform.payment.infrastructure.gateways.paypal.services;

import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.GatewayReference;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.Money;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.PaymentResult;
import com.cargolinklabs.loadmatch.platform.payment.domain.model.valueobjects.PaymentToken;
import com.cargolinklabs.loadmatch.platform.payment.domain.services.PaymentGatewayService;
import com.cargolinklabs.loadmatch.platform.payment.infrastructure.gateways.paypal.PayPalOrdersClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static com.cargolinklabs.loadmatch.platform.payment.infrastructure.gateways.paypal.PayPalResponses.*;

/**
 * Payment gateway implementation that captures an order already approved by the shipper in PayPal
 * <p>
 *     PayPal sandbox does not accept PEN, so the amount is charged with the same value in the configured
 *     PAYPAL_CURRENCY (USD by default). The order amount is verified before capturing it.
 * </p>
 */
@Service
public class PayPalPaymentGatewayService implements PaymentGatewayService {
    private static final String APPROVED = "APPROVED";
    private static final String COMPLETED = "COMPLETED";

    private final PayPalOrdersClient ordersClient;
    private final String currency;

    public PayPalPaymentGatewayService(PayPalOrdersClient ordersClient, @Value("${paypal.currency}") String currency) {
        this.ordersClient = ordersClient;
        this.currency = currency;
    }

    @Override
    public PaymentResult executeCharge(PaymentToken token, Money amount) {
        if (!ordersClient.isConfigured()) {
            return PaymentResult.rejected("GATEWAY_NOT_CONFIGURED");
        }
        try {
            var order = ordersClient.fetchOrder(token.value());
            if (order == null || !APPROVED.equals(order.get("status"))) {
                return PaymentResult.rejected("ORDER_NOT_APPROVED");
            }
            if (!matches(nested(firstOf(order.get("purchase_units")), "amount"), amount)) {
                return PaymentResult.rejected("AMOUNT_MISMATCH");
            }
            var captured = ordersClient.captureOrder(token.value());
            var capture = captured == null ? Map.of()
                    : firstOf(nested(firstOf(captured.get("purchase_units")), "payments").get("captures"));
            if (!COMPLETED.equals(capture.get("status"))) {
                return PaymentResult.rejected("CAPTURE_NOT_COMPLETED");
            }
            return PaymentResult.approved(new GatewayReference(String.valueOf(capture.get("id")), LocalDateTime.now()));
        } catch (RestClientResponseException e) {
            return PaymentResult.rejected(issueOf(e));
        } catch (RestClientException e) {
            return PaymentResult.rejected("GATEWAY_UNAVAILABLE");
        }
    }

    private boolean matches(Map<?, ?> orderAmount, Money amount) {
        return currency.equals(orderAmount.get("currency_code")) && orderAmount.get("value") != null
                && new BigDecimal(String.valueOf(orderAmount.get("value"))).compareTo(amount.amount()) == 0;
    }
}
