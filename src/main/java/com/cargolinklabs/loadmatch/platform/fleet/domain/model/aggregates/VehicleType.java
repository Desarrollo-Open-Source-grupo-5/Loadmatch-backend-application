package com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleTypeId;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

/**
 * Vehicle type aggregate root
 * <p>
 *     Catalog entry that defines the maximum payload admitted for a kind of vehicle.
 * </p>
 */
@Getter
public class VehicleType extends AbstractDomainAggregateRoot<VehicleType> {
    private VehicleTypeId id;
    private String name;
    private String description;
    private double maxWeightKg;
    private boolean active;

    public VehicleType(String name, String description, double maxWeightKg) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Vehicle type name cannot be null or blank");
        }
        if (maxWeightKg <= 0) {
            throw new IllegalArgumentException("Maximum weight must be greater than zero");
        }
        this.id = new VehicleTypeId();
        this.name = name.trim();
        this.description = description;
        this.maxWeightKg = maxWeightKg;
        this.active = true;
    }

    /**
     * Rebuilds a vehicle type from its persisted state
     */
    public static VehicleType reconstitute(VehicleTypeId id, String name, String description,
                                           double maxWeightKg, boolean active) {
        var vehicleType = new VehicleType(name, description, maxWeightKg);
        vehicleType.id = id;
        vehicleType.active = active;
        return vehicleType;
    }

    /**
     * @param weightKg the weight to transport
     * @return true when this vehicle type admits the weight
     */
    public boolean acceptsLoad(double weightKg) {
        return this.active && weightKg <= this.maxWeightKg;
    }
}
