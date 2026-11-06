package com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.events.UserRegisteredEvent;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.EmailAddress;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.Roles;
import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.UserId;
import com.cargolinklabs.loadmatch.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * User aggregate root
 * <p>
 *     Encapsulates the credentials and the lifecycle of a LoadMatch account.
 *     The password is always stored hashed; hashing is delegated to the application layer.
 * </p>
 */
@Getter
public class User extends AbstractDomainAggregateRoot<User> {
    private UserId id;
    private EmailAddress email;
    private String password;
    private Roles role;
    private boolean active;
    private LocalDateTime lastAccessAt;

    /**
     * Creates a new active user
     * @param email the email address used to sign in
     * @param hashedPassword the already hashed password
     * @param role the role of the user
     */
    public User(EmailAddress email, String hashedPassword, Roles role) {
        if (hashedPassword == null || hashedPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.id = new UserId();
        this.email = email;
        this.password = hashedPassword;
        this.role = role;
        this.active = true;
    }

    /**
     * Rebuilds a user from its persisted state without raising domain events
     */
    public static User reconstitute(UserId id, EmailAddress email, String password, Roles role,
                                    boolean active, LocalDateTime lastAccessAt) {
        var user = new User(email, password, role);
        user.id = id;
        user.active = active;
        user.lastAccessAt = lastAccessAt;
        return user;
    }

    /**
     * Registers the event that announces the new account
     */
    public void onRegistered() {
        this.registerDomainEvent(new UserRegisteredEvent(this.id.userId(), this.email.address(), this.role));
    }

    /**
     * Records a successful sign in
     */
    public void recordAccess() {
        this.lastAccessAt = LocalDateTime.now();
    }

    /**
     * Deactivates the account so it can no longer sign in
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * @return true when the user can sign in
     */
    public boolean isEnabled() {
        return this.active;
    }

    /**
     * @return the role name (e.g., ROLE_SHIPPER)
     */
    public String getRoleName() {
        return this.role.name();
    }
}
