package com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.assemblers;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates.Shipper;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.profiles.infrastructure.persistence.jpa.entities.ShipperPersistenceEntity;

/**
 * Assembler that translates between the shipper aggregate and its persistence entity
 */
public final class ShipperPersistenceAssembler {

    private ShipperPersistenceAssembler() {
    }

    public static ShipperPersistenceEntity toPersistenceFromDomain(Shipper shipper) {
        var entity = new ShipperPersistenceEntity();
        entity.setId(shipper.getId().shipperId());
        entity.setUserId(shipper.getUserId().userId());
        entity.setRuc(shipper.getRuc().ruc());
        entity.setBusinessName(shipper.getBusinessName());
        entity.setContactName(shipper.getContactName());
        entity.setPhoneNumber(shipper.getPhoneNumber().phoneNumber());
        entity.setFiscalStreet(shipper.getFiscalAddress().street());
        entity.setFiscalDistrict(shipper.getFiscalAddress().district());
        entity.setFiscalProvince(shipper.getFiscalAddress().province());
        entity.setFiscalDepartment(shipper.getFiscalAddress().department());
        return entity;
    }

    public static Shipper toDomainFromPersistence(ShipperPersistenceEntity entity) {
        return Shipper.reconstitute(
                new ShipperId(entity.getId()),
                new UserId(entity.getUserId()),
                new Ruc(entity.getRuc()),
                entity.getBusinessName(),
                entity.getContactName(),
                new PhoneNumber(entity.getPhoneNumber()),
                new Address(entity.getFiscalStreet(), entity.getFiscalDistrict(),
                        entity.getFiscalProvince(), entity.getFiscalDepartment()));
    }
}
