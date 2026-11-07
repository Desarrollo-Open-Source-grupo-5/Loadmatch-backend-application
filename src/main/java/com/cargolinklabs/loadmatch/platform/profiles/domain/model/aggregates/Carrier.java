package com.cargolinklabs.loadmatch.platform.profiles.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.profiles.domain.model.events.CarrierEnabledEvent;
import com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects.*;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

/**
 * Carrier aggregate root
 * AU
 * <p>
 *     Represents the independent carrier that transports loads. A new carrier stays PENDING until its
 *     documentation is validated; only an ENABLED carrier can accept trips.
 * </p>
 */
@Getter
public class Carrier extends AbstractDomainAggregateRoot<Carrier> {
    private CarrierId id;
    private UserId userId;
    private String firstNames;
    private String lastNames;
    private Dni dni;
    private DriverLicense license;
    private PhoneNumber phoneNumber;
    private String operatingZone;
    private String photoUrl;
    private EnablementStatus enablementStatus;

    public Carrier(UserId userId, String firstNames, String lastNames, Dni dni, DriverLicense license,
                   PhoneNumber phoneNumber, String operatingZone) {
        if (userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if (firstNames == null || firstNames.isBlank()) {
            throw new IllegalArgumentException("First names cannot be null or blank");
        }
        if (lastNames == null || lastNames.isBlank()) {
            throw new IllegalArgumentException("Last names cannot be null or blank");
        }
        if (license == null || !license.isValid()) {
            throw new IllegalArgumentException("Driver license must be valid and not expired");
        }
        this.id = new CarrierId();
        this.userId = userId;
        this.firstNames = firstNames.trim();
        this.lastNames = lastNames.trim();
        this.dni = dni;
        this.license = license;
        this.phoneNumber = phoneNumber;
        this.operatingZone = operatingZone;
        this.enablementStatus = EnablementStatus.PENDING;
    }

    private Carrier() {
    }

    /**
     * Rebuilds a carrier from its persisted state without validating creation rules
     * (for example, a license that expired after the registration)
     */
    public static Carrier reconstitute(CarrierId id, UserId userId, String firstNames, String lastNames, Dni dni,
                                       DriverLicense license, PhoneNumber phoneNumber, String operatingZone,
                                       String photoUrl, EnablementStatus enablementStatus) {
        var carrier = new Carrier();
        carrier.id = id;
        carrier.userId = userId;
        carrier.firstNames = firstNames;
        carrier.lastNames = lastNames;
        carrier.dni = dni;
        carrier.license = license;
        carrier.phoneNumber = phoneNumber;
        carrier.operatingZone = operatingZone;
        carrier.photoUrl = photoUrl;
        carrier.enablementStatus = enablementStatus;
        return carrier;
    }

    /**
     * Enables the carrier to accept trips once its documentation is validated
     */
    public void enable() {
        if (this.enablementStatus == EnablementStatus.ENABLED) {
            return;
        }
        this.enablementStatus = EnablementStatus.ENABLED;
        this.registerDomainEvent(new CarrierEnabledEvent(this.id.carrierId()));
    }

    /**
     * Updates the contact data shown in the public profile of the carrier
     * @param phoneNumber the new phone number
     * @param operatingZone the new operating zone
     * @param photoUrl the new profile photo URL
     */
    public void updateProfile(PhoneNumber phoneNumber, String operatingZone, String photoUrl) {
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Phone number cannot be null");
        }
        this.phoneNumber = phoneNumber;
        this.operatingZone = operatingZone;
        this.photoUrl = photoUrl;
    }

    /**
     * @return true when the carrier is enabled and its license is still valid
     */
    public boolean canAcceptTrips() {
        return this.enablementStatus == EnablementStatus.ENABLED && this.license.isValid();
    }

    /**
     * @return the full name of the carrier
     */
    public String getFullName() {
        return "%s %s".formatted(this.firstNames, this.lastNames);
    }
}
