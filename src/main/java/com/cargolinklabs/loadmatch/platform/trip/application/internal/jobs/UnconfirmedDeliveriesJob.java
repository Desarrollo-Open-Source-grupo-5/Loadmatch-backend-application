package com.cargolinklabs.loadmatch.platform.trip.application.internal.jobs;

import com.cargolinklabs.loadmatch.platform.trip.application.commandservices.TripCommandService;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.CompleteUnconfirmedDeliveriesCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job that completes the deliveries the shipper did not confirm nor observe within 24 hours
 */
@Component
public class UnconfirmedDeliveriesJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnconfirmedDeliveriesJob.class);
    private final TripCommandService tripCommandService;

    public UnconfirmedDeliveriesJob(TripCommandService tripCommandService) {
        this.tripCommandService = tripCommandService;
    }

    /**
     * Runs every hour
     */
    @Scheduled(fixedDelayString = "PT1H", initialDelayString = "PT1M")
    public void completeUnconfirmedDeliveries() {
        var completedTrips = tripCommandService.handle(new CompleteUnconfirmedDeliveriesCommand());
        if (completedTrips > 0) {
            LOGGER.info("{} delivered trips were completed automatically", completedTrips);
        }
    }
}
