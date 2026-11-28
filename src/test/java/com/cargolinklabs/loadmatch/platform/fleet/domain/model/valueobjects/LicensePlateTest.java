package com.cargolinklabs.loadmatch.platform.fleet.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LicensePlateTest {

    @Test
    void constructorNormalizesValidPlate() {
        var plate = new LicensePlate(" abc 123 ");
        assertEquals("ABC-123", plate.licensePlate());
    }

    @Test
    void constructorWithInvalidFormatThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new LicensePlate("12-34"));
    }

    @Test
    void constructorWithNullThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new LicensePlate(null));
    }
}
