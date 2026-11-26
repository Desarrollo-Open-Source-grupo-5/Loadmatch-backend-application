package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DniTest {

    @Test
    void constructorWithValidDniCreatesInstance() {
        var dni = new Dni("12345678");

        assertEquals("12345678", dni.dni());
    }

    @Test
    void constructorWithNullDniThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Dni(null));
    }

    @Test
    void constructorWithInvalidFormatThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Dni("1234"));
    }
}

