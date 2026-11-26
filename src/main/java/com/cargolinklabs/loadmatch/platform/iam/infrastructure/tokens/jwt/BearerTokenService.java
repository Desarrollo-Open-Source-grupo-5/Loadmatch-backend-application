package com.cargolinklabs.loadmatch.platform.iam.infrastructure.tokens.jwt;

import com.cargolinklabs.loadmatch.platform.iam.application.internal.outboundservices.tokens.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * Bearer token service
 * <p>
 *     Extends the {@link TokenService} port with the operations that depend on the
 *     HTTP Authorization header and on the Spring Security authentication object.
 * </p>
 */
public interface BearerTokenService extends TokenService {

    /**
     * Extracts the bearer token from the HTTP request
     *
     * @param request the HTTP request
     * @return the token value, or null when the request has no bearer token
     */
    String getBearerTokenFrom(HttpServletRequest request);

    /**
     * Generates a token from an authentication object
     *
     * @param authentication the authentication object
     * @return the signed token value
     * @see Authentication
     */
    String generateToken(Authentication authentication);
}
