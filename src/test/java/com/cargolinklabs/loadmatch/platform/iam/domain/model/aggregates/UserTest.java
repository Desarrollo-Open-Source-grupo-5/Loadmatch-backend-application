package com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.EmailAddress;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.Roles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructorWithValidDataCreatesActiveUser() {
        var user = new User(new EmailAddress("shipper1@loadmatch.demo"), "hashed-password", Roles.ROLE_SHIPPER);

        assertNotNull(user.getId());
        assertTrue(user.isEnabled());
        assertEquals("ROLE_SHIPPER", user.getRoleName());
    }

    @Test
    void constructorWithBlankPasswordThrowsException() {
        var email = new EmailAddress("shipper1@loadmatch.demo");

        assertThrows(IllegalArgumentException.class, () -> new User(email, " ", Roles.ROLE_SHIPPER));
    }

    @Test
    void constructorWithNullRoleThrowsException() {
        var email = new EmailAddress("carrier1@loadmatch.demo");

        assertThrows(IllegalArgumentException.class, () -> new User(email, "hashed-password", null));
    }

    @Test
    void recordAccessSetsLastAccessDate() {
        var user = new User(new EmailAddress("carrier1@loadmatch.demo"), "hashed-password", Roles.ROLE_CARRIER);

        user.recordAccess();

        assertNotNull(user.getLastAccessAt());
    }

    @Test
    void deactivateDisablesUser() {
        var user = new User(new EmailAddress("carrier1@loadmatch.demo"), "hashed-password", Roles.ROLE_CARRIER);

        user.deactivate();

        assertFalse(user.isEnabled());
    }
}
