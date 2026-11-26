package com.cargolinklabs.loadmatch.platform.iam.infrastructure.authorization.sfs.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

/**
 * Username password authentication token builder
 * <p>
 *     Builds the authentication object that is stored in the security context once the
 *     bearer token of a request has been validated.
 * </p>
 */
public class UsernamePasswordAuthenticationTokenBuilder {

    /**
     * Builds the authentication token for the authenticated principal
     *
     * @param principal the authenticated user details
     * @param request the HTTP request
     * @return the authentication token with the principal authorities
     */
    public static UsernamePasswordAuthenticationToken build(UserDetails principal, HttpServletRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }
}
