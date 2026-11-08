package com.cargolinklabs.loadmatch.platform.trip.domain.model.entities;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Trip status history entity
 * <p>
 *     Records each transition of a trip. It can only be created by the Trip aggregate root.
 * </p>
 */
@Getter
public class TripStatusHistory {
    private UUID id;
    private TripStatus previousStatus;
    private TripStatus newStatus;
    private String description;
    private LocalDateTime registeredAt;

    TripStatusHistory(TripStatus previousStatus, TripStatus newStatus, String description) {
        this.id = UUID.randomUUID();
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.description = description;
        this.registeredAt = LocalDateTime.now();
    }

    private TripStatusHistory() {
    }

    /**
     * Creates a history entry for a transition of the trip
     */
    public static TripStatusHistory of(TripStatus previousStatus, TripStatus newStatus, String description) {
        return new TripStatusHistory(previousStatus, newStatus, description);
    }

    /**
     * Rebuilds a history entry from its persisted state
     */
    public static TripStatusHistory reconstitute(UUID id, TripStatus previousStatus, TripStatus newStatus,
                                                 String description, LocalDateTime registeredAt) {
        var history = new TripStatusHistory();
        history.id = id;
        history.previousStatus = previousStatus;
        history.newStatus = newStatus;
        history.description = description;
        history.registeredAt = registeredAt;
        return history;
    }
}
