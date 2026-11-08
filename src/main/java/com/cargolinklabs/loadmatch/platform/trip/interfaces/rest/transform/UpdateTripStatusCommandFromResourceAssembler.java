package com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.transform;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.commands.UpdateTripStatusCommand;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripStatus;
import com.cargolinklabs.loadmatch.platform.trip.interfaces.rest.resources.UpdateTripStatusResource;

import java.util.UUID;

/**
 * Assembler that translates a trip status update resource into a command
 */
public final class UpdateTripStatusCommandFromResourceAssembler {

    private UpdateTripStatusCommandFromResourceAssembler() {
    }

    public static UpdateTripStatusCommand toCommandFromResource(UUID tripId, UpdateTripStatusResource resource) {
        return new UpdateTripStatusCommand(
                new TripId(tripId),
                toTripStatus(resource.status()),
                resource.description(),
                resource.evidenceUrl());
    }

    private static TripStatus toTripStatus(String status) {
        try {
            return TripStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown trip status: %s".formatted(status));
        }
    }
}
