package com.cargolinklabs.loadmatch.platform.trip.infrastructure.scheduling.spring.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring scheduling configuration
 * <p>
 *     Enables the scheduled jobs of the Trip Execution bounded context.
 * </p>
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration {
}
