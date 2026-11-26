package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RucTest {

    @Test
    void constructorWithValidRucCreatesInstance() {
        var ruc = new Ruc("20100047218");

        assertEquals("20100047218", ruc.ruc());
    }

    @Test
    void constructorWithInvalidLengthThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Ruc("12345"));
    }

    @Test
    void constructorWithInvalidCheckDigitThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Ruc("20100047219"));
    }
}

