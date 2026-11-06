package com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.events.ShipperRegisteredEvent;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.Address;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.PhoneNumber;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.Ruc;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.UserId;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

/**
 * Shipper aggregate root
 * <p>
 *     Represents the business (company) that publishes load requests. The RUC and the business name
 *     identify the company and cannot be changed by the shipper.
 * </p>
 */
@Getter
public class Shipper extends AbstractDomainAggregateRoot<Shipper> {
    private ShipperId id;
    private UserId userId;
    private Ruc ruc;
    private String businessName;
    private String contactName;
    private PhoneNumber phoneNumber;
    private Address fiscalAddress;

    public Shipper(UserId userId, Ruc ruc, String businessName, String contactName,
                   PhoneNumber phoneNumber, Address fiscalAddress) {
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if (businessName == null || businessName.isBlank()) {
            throw new IllegalArgumentException("Business name cannot be null or blank");
        }
        if (contactName == null || contactName.isBlank()) {
            throw new IllegalArgumentException("Contact name cannot be null or blank");
        }
        this.id = new ShipperId();
        this.userId = userId;
        this.ruc = ruc;
        this.businessName = businessName.trim();
        this.contactName = contactName.trim();
        this.phoneNumber = phoneNumber;
        this.fiscalAddress = fiscalAddress;
    }

    /**
     * Rebuilds a shipper from its persisted state without raising domain events
     */
    public static Shipper reconstitute(ShipperId id, UserId userId, Ruc ruc, String businessName,
                                       String contactName, PhoneNumber phoneNumber, Address fiscalAddress) {
        var shipper = new Shipper(userId, ruc, businessName, contactName, phoneNumber, fiscalAddress);
        shipper.id = id;
        return shipper;
    }

    /**
     * Registers the event that announces the new shipper
     */
    public void onRegistered() {
        this.registerDomainEvent(new ShipperRegisteredEvent(this.id.shipperId(), this.userId.userId(), this.ruc.ruc()));
    }

    /**
     * Updates the contact person and phone number of the shipper
     * @param contactName the new contact person
     * @param phoneNumber the new phone number
     */
    public void updateContactInfo(String contactName, PhoneNumber phoneNumber) {
        if (contactName == null || contactName.isBlank()) {
            throw new IllegalArgumentException("Contact name cannot be null or blank");
        }
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        this.contactName = contactName.trim();
        this.phoneNumber = phoneNumber;
    }

    /**
     * Updates the fiscal address where carriers pick up the cargo
     * @param fiscalAddress the new address
     */
    public void updateFiscalAddress(Address fiscalAddress) {
        if (fiscalAddress == null) {
            throw new IllegalArgumentException("Fiscal address cannot be null");
        }
        this.fiscalAddress = fiscalAddress;
    }
}
