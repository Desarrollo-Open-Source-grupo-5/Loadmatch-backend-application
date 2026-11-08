package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources;

import java.time.LocalDateTime;

/**
 * Trip status history response resource
 */
public record TripStatusHistoryResource(String previousStatus, String newStatus, String description,
                                        LocalDateTime registeredAt) {
}
