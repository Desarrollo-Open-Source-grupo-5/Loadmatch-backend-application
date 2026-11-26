package com.cargolinklabs.loadmatch.platform.iam.application.internal.outboundservices.tokens;

/**
 * Outbound port for bearer token issuance and validation used by IAM commands and queries
 */
public interface TokenService {

    /**
     * Generates a token for a username
     *
     * @param username the principal username (the user email address)
     * @return the signed token value
     */
    String generateToken(String username);

    /**
     * Extracts the username from a token
     *
     * @param token the token value
     * @return the username embedded in the token
     */
    String getUsernameFromToken(String token);

    /**
     * Validates a token
     *
     * @param token the token value
     * @return true when the token is valid; otherwise false
     */
    boolean validateToken(String token);
}
