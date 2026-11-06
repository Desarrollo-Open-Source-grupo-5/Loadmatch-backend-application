package com.cargolinklabs.loadmatch.platform.freight.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.freight.domain.model.aggregates.LoadRequest;
import com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.freight.infrastructure.persistence.jpa.entities.LoadRequestPersistenceEntity;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.valueobjects.Coordinates;

/**
 * Assembler that translates between the load request aggregate and its persistence entity
 */
public final class LoadRequestPersistenceAssembler {

    private LoadRequestPersistenceAssembler() {
    }

    public static LoadRequestPersistenceEntity toPersistenceFromDomain(LoadRequest loadRequest) {
        var entity = new LoadRequestPersistenceEntity();
        var origin = loadRequest.getRoute().origin();
        var destination = loadRequest.getRoute().destination();
        entity.setId(loadRequest.getId().loadRequestId());
        entity.setShipperId(loadRequest.getShipperId().shipperId());
        entity.setVehicleTypeId(loadRequest.getVehicleTypeId().vehicleTypeId());
        entity.setOriginAddress(origin.address());
        entity.setOriginDistrict(origin.district());
        entity.setOriginLatitude(origin.coordinates().latitude());
        entity.setOriginLongitude(origin.coordinates().longitude());
        entity.setDestinationAddress(destination.address());
        entity.setDestinationDistrict(destination.district());
        entity.setDestinationLatitude(destination.coordinates().latitude());
        entity.setDestinationLongitude(destination.coordinates().longitude());
        entity.setWeightKg(loadRequest.getWeightKg());
        entity.setLengthM(loadRequest.getDimensions().lengthM());
        entity.setWidthM(loadRequest.getDimensions().widthM());
        entity.setHeightM(loadRequest.getDimensions().heightM());
        entity.setCargoType(loadRequest.getCargoType());
        entity.setRateAmount(loadRequest.getOfferedRate().amount());
        entity.setRateCurrency(loadRequest.getOfferedRate().currency());
        entity.setStatus(loadRequest.getStatus());
        entity.setPickupAt(loadRequest.getPickupAt());
        entity.setPublishedAt(loadRequest.getPublishedAt());
        entity.setUrgent(loadRequest.isUrgent());
        return entity;
    }

    public static LoadRequest toDomainFromPersistence(LoadRequestPersistenceEntity entity) {
        var route = new Route(
                new Location(entity.getOriginAddress(), entity.getOriginDistrict(),
                        new Coordinates(entity.getOriginLatitude(), entity.getOriginLongitude())),
                new Location(entity.getDestinationAddress(), entity.getDestinationDistrict(),
                        new Coordinates(entity.getDestinationLatitude(), entity.getDestinationLongitude())));
        return LoadRequest.reconstitute(
                new LoadRequestId(entity.getId()),
                new ShipperId(entity.getShipperId()),
                new VehicleTypeId(entity.getVehicleTypeId()),
                route,
                entity.getWeightKg(),
                new Dimensions(entity.getLengthM(), entity.getWidthM(), entity.getHeightM()),
                entity.getCargoType(),
                new Money(entity.getRateAmount(), entity.getRateCurrency()),
                entity.getStatus(),
                entity.getPickupAt(),
                entity.getPublishedAt(),
                entity.isUrgent());
    }
}
