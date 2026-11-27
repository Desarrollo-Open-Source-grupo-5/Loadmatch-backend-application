package com.cargolinklabs.loadmatch.platform.payment.infrastructure.gateways.paypal;

import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

/**
 * Helpers to read the JSON responses of the PayPal Orders API
 */
public final class PayPalResponses {

    private PayPalResponses() {
    }

    /**
     * @return the first object of a JSON array, or an empty map when it is missing
     */
    public static Map<?, ?> firstOf(Object list) {
        return list instanceof List<?> items && !items.isEmpty() && items.getFirst() instanceof Map<?, ?> first
                ? first : Map.of();
    }

    /**
     * @return the nested JSON object, or an empty map when it is missing
     */
    public static Map<?, ?> nested(Map<?, ?> source, String key) {
        return source != null && source.get(key) instanceof Map<?, ?> value ? value : Map.of();
    }

    /**
     * @return the PayPal issue code of an error response (for example INSTRUMENT_DECLINED), or the HTTP status
     */
    public static String issueOf(RestClientResponseException exception) {
        try {
            var body = exception.getResponseBodyAs(Map.class);
            var issue = body == null ? null : firstOf(body.get("details")).get("issue");
            if (issue != null) {
                return String.valueOf(issue);
            }
        } catch (RuntimeException ignored) {
            // The error body is not JSON: fall back to the HTTP status
        }
        return "GATEWAY_HTTP_" + exception.getStatusCode().value();
    }
}
