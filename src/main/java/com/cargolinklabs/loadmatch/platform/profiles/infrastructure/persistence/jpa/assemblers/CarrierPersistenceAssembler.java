package com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Carrier;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.entities.CarrierPersistenceEntity;

/**
 * Assembler that translates between the carrier aggregate and its persistence entity
 */
public final class CarrierPersistenceAssembler {

    private CarrierPersistenceAssembler() {
    }

    public static CarrierPersistenceEntity toPersistenceFromDomain(Carrier carrier) {
        var entity = new CarrierPersistenceEntity();
        entity.setId(carrier.getId().carrierId());
        entity.setUserId(carrier.getUserId().userId());
        entity.setFirstNames(carrier.getFirstNames());
        entity.setLastNames(carrier.getLastNames());
        entity.setDni(carrier.getDni().dni());
        entity.setLicenseNumber(carrier.getLicense().number());
        entity.setLicenseCategory(carrier.getLicense().category());
        entity.setLicenseExpirationDate(carrier.getLicense().expirationDate());
        entity.setPhoneNumber(carrier.getPhoneNumber().phoneNumber());
        entity.setOperatingZone(carrier.getOperatingZone());
        entity.setPhotoUrl(carrier.getPhotoUrl());
        entity.setEnablementStatus(carrier.getEnablementStatus());
        return entity;
    }

    public static Carrier toDomainFromPersistence(CarrierPersistenceEntity entity) {
        return Carrier.reconstitute(
                new CarrierId(entity.getId()),
                new UserId(entity.getUserId()),
                entity.getFirstNames(),
                entity.getLastNames(),
                new Dni(entity.getDni()),
                new DriverLicense(entity.getLicenseNumber(), entity.getLicenseCategory(),
                        entity.getLicenseExpirationDate()),
                new PhoneNumber(entity.getPhoneNumber()),
                entity.getOperatingZone(),
                entity.getPhotoUrl(),
                entity.getEnablementStatus());
    }
}
