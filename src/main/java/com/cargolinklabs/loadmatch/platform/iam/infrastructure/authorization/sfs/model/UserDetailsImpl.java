package com.cargolinklabs.loadmatch.platform.iam.infrastructure.authorization.sfs.model;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.aggregates.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * User details implementation
 * <p>
 *     Adapts the IAM {@link User} aggregate to the Spring Security {@link UserDetails} contract.
 *     The username is the user email address and the only authority is the user role
 *     (ROLE_SHIPPER or ROLE_CARRIER).
 * </p>
 */
@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String username, String password, boolean enabled,
                           Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = enabled;
    }

    /**
     * Builds the user details from the IAM user aggregate
     *
     * @param user the user aggregate
     * @return the user details
     */
    public static UserDetailsImpl build(User user) {
        var authorities = List.of(new SimpleGrantedAuthority(user.getRoleName()));
        return new UserDetailsImpl(
                user.getEmail().address(),
                user.getPassword(),
                user.isEnabled(),
                authorities);
    }
}
