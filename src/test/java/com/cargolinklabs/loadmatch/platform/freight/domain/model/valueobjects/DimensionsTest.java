package com.cargolinklabs.loadmatch.platform.freight.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DimensionsTest {

    @Test
    void volumeM3ReturnsProductOfPositiveDimensions() {
        var dimensions = new Dimensions(2, 3, 4);

        assertEquals(24.0, dimensions.volumeM3());
    }

    @Test
    void constructorRejectsZeroWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Dimensions(2, 0, 4));
    }

    @Test
    void constructorRejectsNegativeHeight() {
        assertThrows(IllegalArgumentException.class, () -> new Dimensions(2, 3, -1));
    }
}
