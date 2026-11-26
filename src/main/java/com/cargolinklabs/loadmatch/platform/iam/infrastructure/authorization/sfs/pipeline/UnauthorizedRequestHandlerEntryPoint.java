package com.cargolinklabs.loadmatch.platform.iam.infrastructure.authorization.sfs.pipeline;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Unauthorized request handler entry point
 * <p>
 *     Answers HTTP 401 Unauthorized when a request reaches a protected endpoint
 *     without a valid bearer token.
 * </p>
 */
@Component
public class UnauthorizedRequestHandlerEntryPoint implements AuthenticationEntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthorizedRequestHandlerEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException {
        LOGGER.warn("Unauthorized request to {}: {}", request.getRequestURI(), authenticationException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request detected");
    }
}
