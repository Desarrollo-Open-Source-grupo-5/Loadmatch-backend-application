package com.cargolinklabs.loadmatch.platform.freight.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.events.LoadRequestPublishedEvent;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Load request aggregate root
 * <p>
 *     Protects the invariants of a published load: positive weight and dimensions, a future pickup date,
 *     and the transitions between draft, published, assigned, in transit, delivered and cancelled.
 * </p>
 */
@Getter
public class LoadRequest extends AbstractDomainAggregateRoot<LoadRequest> {
    private LoadRequestId id;
    private ShipperId shipperId;
    private VehicleTypeId vehicleTypeId;
    private Route route;
    private double weightKg;
    private Dimensions dimensions;
    private String cargoType;
    private Money offeredRate;
    private LoadRequestStatus status;
    private LocalDateTime pickupAt;
    private LocalDateTime publishedAt;
    private boolean urgent;

    public LoadRequest(ShipperId shipperId, VehicleTypeId vehicleTypeId, Route route, double weightKg,
                       Dimensions dimensions, String cargoType, Money offeredRate, LocalDateTime pickupAt) {
        if (shipperId == null || vehicleTypeId == null || route == null || dimensions == null || offeredRate == null) {
            throw new IllegalArgumentException("Shipper, vehicle type, route, dimensions and rate are required");
        }
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be greater than zero");
        }
        if (cargoType == null || cargoType.isBlank()) {
            throw new IllegalArgumentException("Cargo type cannot be null or blank");
        }
        if (pickupAt == null || !pickupAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Pickup date must be in the future");
        }
        this.id = new LoadRequestId();
        this.shipperId = shipperId;
        this.vehicleTypeId = vehicleTypeId;
        this.route = route;
        this.weightKg = weightKg;
        this.dimensions = dimensions;
        this.cargoType = cargoType.trim();
        this.offeredRate = offeredRate;
        this.pickupAt = pickupAt;
        this.status = LoadRequestStatus.DRAFT;
        this.urgent = false;
    }

    private LoadRequest() {
    }

    /**
     * Rebuilds a load request from its persisted state without validating creation rules
     */
    public static LoadRequest reconstitute(LoadRequestId id, ShipperId shipperId, VehicleTypeId vehicleTypeId,
                                           Route route, double weightKg, Dimensions dimensions, String cargoType,
                                           Money offeredRate, LoadRequestStatus status, LocalDateTime pickupAt,
                                           LocalDateTime publishedAt, boolean urgent) {
        var loadRequest = new LoadRequest();
        loadRequest.id = id;
        loadRequest.shipperId = shipperId;
        loadRequest.vehicleTypeId = vehicleTypeId;
        loadRequest.route = route;
        loadRequest.weightKg = weightKg;
        loadRequest.dimensions = dimensions;
        loadRequest.cargoType = cargoType;
        loadRequest.offeredRate = offeredRate;
        loadRequest.status = status;
        loadRequest.pickupAt = pickupAt;
        loadRequest.publishedAt = publishedAt;
        loadRequest.urgent = urgent;
        return loadRequest;
    }

    /**
     * Publishes the load request so carriers can find it
     */
    public void publish() {
        if (this.status != LoadRequestStatus.DRAFT) {
            throw new IllegalStateException("Only a draft load request can be published");
        }
        this.status = LoadRequestStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
        this.registerDomainEvent(new LoadRequestPublishedEvent(this.id.loadRequestId(), this.shipperId.shipperId()));
    }

    /**
     * Marks the load request as assigned to a carrier
     */
    public void markAsAssigned() {
        ensureStatus(LoadRequestStatus.PUBLISHED, "assigned");
        this.status = LoadRequestStatus.ASSIGNED;
    }

    /**
     * Marks the load request as in transit once the cargo is picked up
     */
    public void markInTransit() {
        ensureStatus(LoadRequestStatus.ASSIGNED, "in transit");
        this.status = LoadRequestStatus.IN_TRANSIT;
    }

    /**
     * Marks the load request as delivered at its destination
     */
    public void markAsDelivered() {
        ensureStatus(LoadRequestStatus.IN_TRANSIT, "delivered");
        this.status = LoadRequestStatus.DELIVERED;
    }

    /**
     * @return true when the load request is published and can be accepted by a carrier
     */
    public boolean isAvailable() {
        return this.status == LoadRequestStatus.PUBLISHED;
    }

    private void ensureStatus(LoadRequestStatus expected, String target) {
        if (this.status != expected) {
            throw new IllegalStateException("A load request in status %s cannot be marked as %s"
                    .formatted(this.status, target));
        }
    }
}
