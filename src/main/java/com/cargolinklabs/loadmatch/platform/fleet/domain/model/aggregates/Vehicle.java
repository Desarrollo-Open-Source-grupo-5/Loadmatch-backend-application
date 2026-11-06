package com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.events.VehicleRegisteredEvent;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.Year;

/**
 * Vehicle aggregate root
 * <p>
 *     A vehicle of the carrier fleet. It starts pending of documentary validation and can be deactivated,
 *     which removes it from the freight search without deleting its history.
 * </p>
 */
@Getter
public class Vehicle extends AbstractDomainAggregateRoot<Vehicle> {
    private static final int MIN_MANUFACTURE_YEAR = 1980;

    private VehicleId id;
    private CarrierId carrierId;
    private VehicleTypeId vehicleTypeId;
    private LicensePlate licensePlate;
    private String brand;
    private String model;
    private int manufactureYear;
    private double payloadKg;
    private MtcValidationStatus mtcValidationStatus;
    private boolean active;

    public Vehicle(CarrierId carrierId, VehicleTypeId vehicleTypeId, LicensePlate licensePlate, String brand,
                   String model, int manufactureYear, double payloadKg) {
        if (carrierId == null || vehicleTypeId == null || licensePlate == null) {
            throw new IllegalArgumentException("Carrier, vehicle type and license plate are required");
        }
        this.id = new VehicleId();
        this.carrierId = carrierId;
        this.licensePlate = licensePlate;
        this.mtcValidationStatus = MtcValidationStatus.PENDING;
        this.active = true;
        updateTechnicalData(vehicleTypeId, brand, model, manufactureYear, payloadKg);
    }

    private Vehicle() {
    }

    /**
     * Rebuilds a vehicle from its persisted state without validating creation rules
     */
    public static Vehicle reconstitute(VehicleId id, CarrierId carrierId, VehicleTypeId vehicleTypeId,
                                       LicensePlate licensePlate, String brand, String model, int manufactureYear,
                                       double payloadKg, MtcValidationStatus mtcValidationStatus, boolean active) {
        var vehicle = new Vehicle();
        vehicle.id = id;
        vehicle.carrierId = carrierId;
        vehicle.vehicleTypeId = vehicleTypeId;
        vehicle.licensePlate = licensePlate;
        vehicle.brand = brand;
        vehicle.model = model;
        vehicle.manufactureYear = manufactureYear;
        vehicle.payloadKg = payloadKg;
        vehicle.mtcValidationStatus = mtcValidationStatus;
        vehicle.active = active;
        return vehicle;
    }

    /**
     * Registers the event that announces the new vehicle
     */
    public void onRegistered() {
        this.registerDomainEvent(new VehicleRegisteredEvent(this.id.vehicleId(), this.carrierId.carrierId(),
                this.licensePlate.licensePlate()));
    }

    /**
     * Updates the technical data of the vehicle
     */
    public void updateTechnicalData(VehicleTypeId vehicleTypeId, String brand, String model,
                                    int manufactureYear, double payloadKg) {
        if (vehicleTypeId == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if (brand == null || brand.isBlank() || model == null || model.isBlank()) {
            throw new IllegalArgumentException("Brand and model cannot be null or blank");
        }
        validateManufactureYear(manufactureYear);
        if (payloadKg <= 0) {
            throw new IllegalArgumentException("Payload must be greater than zero");
        }
        this.vehicleTypeId = vehicleTypeId;
        this.brand = brand.trim();
        this.model = model.trim();
        this.manufactureYear = manufactureYear;
        this.payloadKg = payloadKg;
    }

    /**
     * Deactivates the vehicle so it is no longer considered in the freight search
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Reactivates a deactivated vehicle
     */
    public void reactivate() {
        this.active = true;
    }

    /**
     * @param weightKg the weight to transport
     * @return true when the vehicle is active and can carry the weight
     */
    public boolean canTransport(double weightKg) {
        return this.active && weightKg <= this.payloadKg;
    }

    private void validateManufactureYear(int year) {
        var maxYear = Year.now().getValue() + 1;
        if (year < MIN_MANUFACTURE_YEAR || year > maxYear) {
            throw new IllegalArgumentException("Manufacture year must be between %d and %d"
                    .formatted(MIN_MANUFACTURE_YEAR, maxYear));
        }
    }
}
