package com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.Vehicle;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.entities.VehiclePersistenceEntity;

/**
 * Assembler that translates between the vehicle aggregate and its persistence entity
 */
public final class VehiclePersistenceAssembler {

    private VehiclePersistenceAssembler() {
    }

    public static VehiclePersistenceEntity toPersistenceFromDomain(Vehicle vehicle) {
        var entity = new VehiclePersistenceEntity();
        entity.setId(vehicle.getId().vehicleId());
        entity.setCarrierId(vehicle.getCarrierId().carrierId());
        entity.setVehicleTypeId(vehicle.getVehicleTypeId().vehicleTypeId());
        entity.setLicensePlate(vehicle.getLicensePlate().licensePlate());
        entity.setBrand(vehicle.getBrand());
        entity.setModel(vehicle.getModel());
        entity.setManufactureYear(vehicle.getManufactureYear());
        entity.setPayloadKg(vehicle.getPayloadKg());
        entity.setMtcValidationStatus(vehicle.getMtcValidationStatus());
        entity.setActive(vehicle.isActive());
        return entity;
    }

    public static Vehicle toDomainFromPersistence(VehiclePersistenceEntity entity) {
        return Vehicle.reconstitute(
                new VehicleId(entity.getId()),
                new CarrierId(entity.getCarrierId()),
                new VehicleTypeId(entity.getVehicleTypeId()),
                new LicensePlate(entity.getLicensePlate()),
                entity.getBrand(),
                entity.getModel(),
                entity.getManufactureYear(),
                entity.getPayloadKg(),
                entity.getMtcValidationStatus(),
                entity.isActive());
    }
}
