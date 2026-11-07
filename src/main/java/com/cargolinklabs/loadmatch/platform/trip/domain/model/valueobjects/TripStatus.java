package com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects;

import java.util.EnumSet;
import java.util.Set;

/**
 * Status of a trip and its allowed transitions
 */
public enum TripStatus {
    ASSIGNED,
    EN_ROUTE_TO_PICKUP,
    AT_PICKUP_POINT,
    CARGO_PICKED_UP,
    IN_TRANSIT,
    DELAYED,
    DELIVERED,
    DISPUTED,
    COMPLETED,
    CANCELLED;

    /**
     * @param target the target status
     * @return true when the trip can move from this status to the target status
     */
    public boolean canTransitionTo(TripStatus target) {
        return allowedTransitions().contains(target);
    }

    /**
     * @return true when the trip cannot change anymore
     */
    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED;
    }

    private Set<TripStatus> allowedTransitions() {
        return switch (this) {
            case ASSIGNED -> EnumSet.of(EN_ROUTE_TO_PICKUP, CANCELLED);
            case EN_ROUTE_TO_PICKUP -> EnumSet.of(AT_PICKUP_POINT, DELAYED, CANCELLED);
            case AT_PICKUP_POINT -> EnumSet.of(CARGO_PICKED_UP, CANCELLED);
            case CARGO_PICKED_UP -> EnumSet.of(IN_TRANSIT);
            case IN_TRANSIT -> EnumSet.of(DELAYED, DELIVERED);
            case DELAYED -> EnumSet.of(EN_ROUTE_TO_PICKUP, IN_TRANSIT, DELIVERED);
            case DELIVERED -> EnumSet.of(COMPLETED, DISPUTED);
            case DISPUTED -> EnumSet.of(COMPLETED);
            case COMPLETED, CANCELLED -> EnumSet.noneOf(TripStatus.class);
        };
    }
}
