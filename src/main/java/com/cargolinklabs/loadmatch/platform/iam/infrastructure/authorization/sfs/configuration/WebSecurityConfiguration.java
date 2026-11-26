package com.cargolinklabs.loadmatch.platform.iam.infrastructure.authorization.sfs.configuration;

import com.cargolinklabs.loadmatch.platform.iam.infrastructure.authorization.sfs.pipeline.BearerAuthorizationRequestFilter;
import com.cargolinklabs.loadmatch.platform.iam.infrastructure.tokens.jwt.BearerTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web security configuration
 * <p>
 *     Configures a stateless API protected with bearer tokens (JWT):
 *     public endpoints for sign-in, registration and documentation, role-based access
 *     for the shipper and carrier operations, and authentication for everything else.
 *     CORS rules come from the shared CORS configuration.
 * </p>
 */
@Configuration
public class WebSecurityConfiguration {
    private static final String SHIPPER = "SHIPPER";
    private static final String CARRIER = "CARRIER";

    private final UserDetailsService userDetailsService;
    private final BearerTokenService tokenService;
    private final AuthenticationEntryPoint unauthorizedRequestHandler;

    public WebSecurityConfiguration(@Qualifier("defaultUserDetailsService") UserDetailsService userDetailsService,
                                    BearerTokenService tokenService,
                                    AuthenticationEntryPoint unauthorizedRequestHandler) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
        this.unauthorizedRequestHandler = unauthorizedRequestHandler;
    }

    /**
     * Bearer authorization request filter bean
     *
     * @return the filter that authenticates requests with a valid bearer token
     */
    @Bean
    public BearerAuthorizationRequestFilter authorizationRequestFilter() {
        return new BearerAuthorizationRequestFilter(tokenService, userDetailsService);
    }

    /**
     * Security filter chain
     *
     * @param http the HTTP security builder
     * @return the configured security filter chain
     * @throws Exception when the configuration cannot be built
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(unauthorizedRequestHandler))
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // Public endpoints
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/tokens", "/api/v1/shippers", "/api/v1/carriers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/vehicle-types").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/error").permitAll()
                        // Shipper operations
                        .requestMatchers(HttpMethod.POST, "/api/v1/load-requests").hasRole(SHIPPER)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/shippers/**").hasRole(SHIPPER)
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/trips/*/delivery-confirmation",
                                "/api/v1/trips/*/delivery-observations",
                                "/api/v1/payments").hasRole(SHIPPER)
                        // Carrier operations
                        .requestMatchers(HttpMethod.GET, "/api/v1/available-loads").hasRole(CARRIER)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/carriers/**", "/api/v1/vehicles/**").hasRole(CARRIER)
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/vehicles/**",
                                "/api/v1/trips",
                                "/api/v1/trips/*/status-updates",
                                "/api/v1/trips/*/incidents").hasRole(CARRIER)
                        .requestMatchers("/api/v1/documents/**", "/api/v1/document-types/**").hasRole(CARRIER)
                        // Any other endpoint requires an authenticated user
                        .anyRequest().authenticated());
        http.addFilterBefore(authorizationRequestFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
