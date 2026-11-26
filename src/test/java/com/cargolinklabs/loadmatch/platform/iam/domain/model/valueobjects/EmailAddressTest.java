package com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailAddressTest {

    @Test
    void constructorWithValidEmailCreatesInstance() {
        var email = new EmailAddress("shipper1@loadmatch.demo");

        assertEquals("shipper1@loadmatch.demo", email.address());
    }

    @Test
    void constructorNormalizesEmailToLowerCase() {
        var email = new EmailAddress("  Shipper1@LoadMatch.Demo ");

        assertEquals("shipper1@loadmatch.demo", email.address());
    }

    @Test
    void constructorWithBlankEmailThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new EmailAddress(" "));
    }

    @Test
    void constructorWithInvalidFormatThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new EmailAddress("shipper1.loadmatch.demo"));
    }
}
