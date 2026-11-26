package com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolesTest {

    @Test
    void fromNameWithPrefixReturnsRole() {
        assertEquals(Roles.ROLE_SHIPPER, Roles.fromName("ROLE_SHIPPER"));
    }

    @Test
    void fromNameWithoutPrefixReturnsRole() {
        assertEquals(Roles.ROLE_CARRIER, Roles.fromName("carrier"));
    }

    @Test
    void fromNameWithBlankNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Roles.fromName(" "));
    }

    @Test
    void fromNameWithUnknownNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Roles.fromName("admin"));
    }
}
