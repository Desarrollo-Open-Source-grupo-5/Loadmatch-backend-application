package com.cargolinklabs.loadmatch.platform.iam.infrastructure.tokens.jwt.services;

import com.cargolinklabs.loadmatch.platform.iam.infrastructure.tokens.jwt.BearerTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

/**
 * Token service implementation for JWT tokens
 * <p>
 *     Generates and validates signed JWT tokens (HMAC-SHA) using the secret and the
 *     expiration days configured in the application properties.
 * </p>
 */
@Service
public class TokenServiceImpl implements BearerTokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);
    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_BEGIN_INDEX = 7;

    @Value("${authorization.jwt.secret}")
    private String secret;

    @Value("${authorization.jwt.expiration.days}")
    private int expirationDays;

    /**
     * Generates a JWT token from an authentication object
     *
     * @param authentication the authentication object
     * @return the signed token value
     */
    @Override
    public String generateToken(Authentication authentication) {
        return buildTokenWithDefaultParameters(authentication.getName());
    }

    /**
     * Generates a JWT token from a username
     *
     * @param username the username (the user email address)
     * @return the signed token value
     */
    @Override
    public String generateToken(String username) {
        return buildTokenWithDefaultParameters(username);
    }

    /**
     * Builds a token for the username with the configured expiration days
     *
     * @param username the username
     * @return the signed token value
     */
    private String buildTokenWithDefaultParameters(String username) {
        var issuedAt = Instant.now();
        var expiration = issuedAt.plus(Duration.ofDays(expirationDays));
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the username from a JWT token
     *
     * @param token the token value
     * @return the username
     */
    @Override
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates a JWT token
     *
     * @param token the token value
     * @return true when the token is valid; otherwise false
     */
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JSON Web Token signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JSON Web Token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JSON Web Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JSON Web Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JSON Web Token claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Extracts the bearer token from the Authorization header of the request
     *
     * @param request the HTTP request
     * @return the token value, or null when the header is missing or is not a bearer token
     */
    @Override
    public String getBearerTokenFrom(HttpServletRequest request) {
        var parameter = request.getHeader(AUTHORIZATION_PARAMETER_NAME);
        if (StringUtils.hasText(parameter) && parameter.startsWith(BEARER_TOKEN_PREFIX)) {
            return parameter.substring(TOKEN_BEGIN_INDEX);
        }
        return null;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
