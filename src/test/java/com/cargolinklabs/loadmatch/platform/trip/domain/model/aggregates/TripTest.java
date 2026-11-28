package com.cargolinklabs.loadmatch.platform.trip.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.CarrierId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.LoadRequestId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.ShipperId;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.TripStatus;
import com.cargolinklabs.loadmatch.platform.trip.domain.model.valueobjects.VehicleId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TripTest {

    @Test
    void constructorAssignsInitialStatusAndHistory() {
        var trip = new Trip(
                new LoadRequestId(UUID.randomUUID()),
                new ShipperId(UUID.randomUUID()),
                new CarrierId(UUID.randomUUID()),
                new VehicleId(UUID.randomUUID())
        );

        assertEquals(TripStatus.ASSIGNED, trip.getStatus());
        assertTrue(trip.isInProgress());
        assertEquals(1, trip.getHistory().size());
    }

    @Test
    void updateStatusToDeliveredWithoutEvidenceThrowsException() {
        var trip = new Trip(
                new LoadRequestId(UUID.randomUUID()),
                new ShipperId(UUID.randomUUID()),
                new CarrierId(UUID.randomUUID()),
                new VehicleId(UUID.randomUUID())
        );

        assertThrows(IllegalArgumentException.class,
                () -> trip.updateStatus(TripStatus.DELIVERED, "Delivered", " "));
    }

    @Test
    void confirmReceiptBeforeDeliveryThrowsException() {
        var trip = new Trip(
                new LoadRequestId(UUID.randomUUID()),
                new ShipperId(UUID.randomUUID()),
                new CarrierId(UUID.randomUUID()),
                new VehicleId(UUID.randomUUID())
        );

        assertThrows(IllegalStateException.class, trip::confirmReceipt);
    }
}

