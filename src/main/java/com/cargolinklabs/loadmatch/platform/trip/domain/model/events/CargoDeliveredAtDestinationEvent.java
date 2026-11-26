package com.cargolinklabs.loadmatch.platform.trip.domain.model.events;

import java.util.UUID;

public record CargoDeliveredAtDestinationEvent(UUID tripId, UUID shipperId, UUID carrierId) {
}
