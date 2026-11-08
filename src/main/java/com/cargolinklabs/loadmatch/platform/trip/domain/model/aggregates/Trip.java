package com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.entities.TripStatusHistory;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.events.TripCreatedEvent;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Trip aggregate root
 * <p>
 *     Implements the state machine of a trip, from the assignment to the carrier until its completion.
 *     Every transition is validated and recorded in the trip status history.
 * </p>
 */
@Getter
public class Trip extends AbstractDomainAggregateRoot<Trip> {
    private TripId id;
    private LoadRequestId loadRequestId;
    private ShipperId shipperId;
    private CarrierId carrierId;
    private VehicleId vehicleId;
    private TripStatus status;
    private LocalDateTime assignedAt;
    private LocalDateTime deliveredAt;
    private String deliveryEvidenceUrl;
    private String deliveryObservation;
    private LocalDateTime completedAt;
    private List<TripStatusHistory> history;

    public Trip(LoadRequestId loadRequestId, ShipperId shipperId, CarrierId carrierId, VehicleId vehicleId) {
        if (loadRequestId == null || shipperId == null || carrierId == null || vehicleId == null) {
            throw new IllegalArgumentException("Load request, shipper, carrier and vehicle are required");
        }
        this.id = new TripId();
        this.loadRequestId = loadRequestId;
        this.shipperId = shipperId;
        this.carrierId = carrierId;
        this.vehicleId = vehicleId;
        this.status = TripStatus.ASSIGNED;
        this.assignedAt = LocalDateTime.now();
        this.history = new ArrayList<>();
        this.history.add(TripStatusHistory.of(null, TripStatus.ASSIGNED, "Load request accepted by the carrier"));
    }

    private Trip() {
    }

    /**
     * Rebuilds a trip from its persisted state without raising domain events
     */
    public static Trip reconstitute(TripId id, LoadRequestId loadRequestId, ShipperId shipperId, CarrierId carrierId,
                                    VehicleId vehicleId, TripStatus status, LocalDateTime assignedAt,
                                    LocalDateTime deliveredAt, String deliveryEvidenceUrl, String deliveryObservation,
                                    LocalDateTime completedAt, List<TripStatusHistory> history) {
        var trip = new Trip();
        trip.id = id;
        trip.loadRequestId = loadRequestId;
        trip.shipperId = shipperId;
        trip.carrierId = carrierId;
        trip.vehicleId = vehicleId;
        trip.status = status;
        trip.assignedAt = assignedAt;
        trip.deliveredAt = deliveredAt;
        trip.deliveryEvidenceUrl = deliveryEvidenceUrl;
        trip.deliveryObservation = deliveryObservation;
        trip.completedAt = completedAt;
        trip.history = new ArrayList<>(history);
        return trip;
    }

    /**
     * Registers the event that notifies the shipper that its load was accepted
     */
    public void onCreated() {
        this.registerDomainEvent(new TripCreatedEvent(this.id.tripId(), this.loadRequestId.loadRequestId(),
                this.shipperId.shipperId(), this.carrierId.carrierId()));
    }

    /**
     * @return the status history, oldest first
     */
    public List<TripStatusHistory> getHistory() {
        return Collections.unmodifiableList(this.history);
    }

    /**
     * @return true when the trip has not reached a final status
     */
    public boolean isInProgress() {
        return !this.status.isFinal();
    }

    /**
     * Updates the status of the trip as reported by the carrier
     * <p>
     *     The delivery requires the evidence of the delivered cargo (photo or signed guide URL).
     * </p>
     * @param newStatus the new status (EN_ROUTE_TO_PICKUP, AT_PICKUP_POINT, CARGO_PICKED_UP, IN_TRANSIT, DELAYED or DELIVERED)
     * @param description the description of the update
     * @param evidenceUrl the URL of the delivery evidence, required to mark the trip as DELIVERED
     */
    public void updateStatus(TripStatus newStatus, String description, String evidenceUrl) {
        if (newStatus == TripStatus.COMPLETED || newStatus == TripStatus.DISPUTED || newStatus == TripStatus.CANCELLED) {
            throw new IllegalStateException("Status %s cannot be reported by the carrier".formatted(newStatus));
        }
        if (newStatus == TripStatus.DELIVERED) {
            if (evidenceUrl == null || evidenceUrl.isBlank()) {
                throw new IllegalArgumentException("The delivery evidence is required to mark the trip as delivered");
            }
            this.deliveryEvidenceUrl = evidenceUrl;
            this.deliveredAt = LocalDateTime.now();
        }
        transitionTo(newStatus, description != null && !description.isBlank() ? description : "Status updated by the carrier");
    }

    /**
     * The shipper confirms that the cargo was received as agreed, which completes the trip
     */
    public void confirmReceipt() {
        ensureDelivered();
        transitionTo(TripStatus.COMPLETED, "Delivery confirmed by the shipper");
        this.completedAt = LocalDateTime.now();
    }

    /**
     * The shipper registers an observation about the delivery, which moves the trip to DISPUTED
     * @param observation the reason of the observation
     */
    public void disputeDelivery(String observation) {
        ensureDelivered();
        if (observation == null || observation.isBlank()) {
            throw new IllegalArgumentException("The observation about the delivery is required");
        }
        this.deliveryObservation = observation.trim();
        transitionTo(TripStatus.DISPUTED, "Delivery observed by the shipper: %s".formatted(this.deliveryObservation));
    }

    /**
     * Completes a delivered trip when the shipper did not confirm nor observe the delivery in time
     */
    public void completeWithoutResponse() {
        ensureDelivered();
        transitionTo(TripStatus.COMPLETED, "Completed automatically: the shipper did not respond within 24 hours");
        this.completedAt = LocalDateTime.now();
    }

    /**
     * @param hours the waiting time
     * @return true when the trip was delivered and the shipper has not responded for more than the given hours
     */
    public boolean isAwaitingConfirmationLongerThan(long hours) {
        return this.status == TripStatus.DELIVERED && this.deliveredAt != null
                && this.deliveredAt.isBefore(LocalDateTime.now().minusHours(hours));
    }

    private void ensureDelivered() {
        if (this.status != TripStatus.DELIVERED) {
            throw new IllegalStateException("Only a delivered trip can be confirmed or observed by the shipper");
        }
    }

    /**
     * Moves the trip to a new status after validating the transition and records it in the history
     * @param newStatus the target status
     * @param description the description of the transition
     */
    protected void transitionTo(TripStatus newStatus, String description) {
        validateTransition(newStatus);
        this.history.add(TripStatusHistory.of(this.status, newStatus, description));
        this.status = newStatus;
    }

    private void validateTransition(TripStatus newStatus) {
        if (newStatus == null || !this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException("A trip cannot move from %s to %s".formatted(this.status, newStatus));
        }
    }
}
