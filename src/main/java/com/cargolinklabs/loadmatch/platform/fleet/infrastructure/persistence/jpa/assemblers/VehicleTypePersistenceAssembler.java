package com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.fleet.domain.model.aggregates.VehicleType;
import com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects.VehicleTypeId;
import com.cargolinklabs.loadmatch.platform.fleet.infrastructure.persistence.jpa.entities.VehicleTypePersistenceEntity;

/**
 * Assembler that translates between the vehicle type aggregate and its persistence entity
 */
public final class VehicleTypePersistenceAssembler {

    private VehicleTypePersistenceAssembler() {
    }

    public static VehicleTypePersistenceEntity toPersistenceFromDomain(VehicleType vehicleType) {
        var entity = new VehicleTypePersistenceEntity();
        entity.setId(vehicleType.getId().vehicleTypeId());
        entity.setName(vehicleType.getName());
        entity.setDescription(vehicleType.getDescription());
        entity.setMaxWeightKg(vehicleType.getMaxWeightKg());
        entity.setActive(vehicleType.isActive());
        return entity;
    }

    public static VehicleType toDomainFromPersistence(VehicleTypePersistenceEntity entity) {
        return VehicleType.reconstitute(
                new VehicleTypeId(entity.getId()),
                entity.getName(),
                entity.getDescription(),
                entity.getMaxWeightKg(),
                entity.isActive());
    }
}
