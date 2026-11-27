package com.cargolinklabs.loadmatch.platform.payment.infrastructure.gateways.paypal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * HTTP client of the PayPal Orders API (sandbox by default)
 * <p>
 *     Credentials are read only from the PAYPAL_CLIENT_ID and PAYPAL_CLIENT_SECRET environment variables.
 * </p>
 */
@Component
public class PayPalOrdersClient {
    private final RestClient restClient;
    private final String clientId;
    private final String clientSecret;

    public PayPalOrdersClient(@Value("${paypal.base-url}") String baseUrl,
                              @Value("${paypal.client-id}") String clientId,
                              @Value("${paypal.client-secret}") String clientSecret) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public boolean isConfigured() {
        return !clientId.isBlank() && !clientSecret.isBlank();
    }

    public Map<?, ?> fetchOrder(String orderId) {
        return restClient.get().uri("/v2/checkout/orders/{orderId}", orderId)
                .headers(headers -> headers.setBearerAuth(requestAccessToken()))
                .retrieve()
                .body(Map.class);
    }

    public Map<?, ?> captureOrder(String orderId) {
        return restClient.post().uri("/v2/checkout/orders/{orderId}/capture", orderId)
                .headers(headers -> headers.setBearerAuth(requestAccessToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of())
                .retrieve()
                .body(Map.class);
    }

    private String requestAccessToken() {
        var form = new LinkedMultiValueMap<String, String>();
        form.add("grant_type", "client_credentials");
        var response = restClient.post().uri("/v1/oauth2/token")
                .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);
        return response == null ? "" : String.valueOf(response.get("access_token"));
    }
}
