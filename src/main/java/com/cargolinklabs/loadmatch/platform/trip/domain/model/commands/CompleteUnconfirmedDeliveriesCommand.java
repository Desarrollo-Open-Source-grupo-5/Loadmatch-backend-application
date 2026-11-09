package com.cargolinklabs.loadmatch.platform.trip.domain.model.commands;

/**
 * Command to complete the delivered trips that the shipper did not confirm nor observe in time
 */
public record CompleteUnconfirmedDeliveriesCommand() {
    /**
     * Hours the shipper has to confirm or observe a delivery
     */
    public static final long CONFIRMATION_WINDOW_HOURS = 24;
}
