package com.cargolinklabs.loadmatch.platform.iam.infrastructure.authorization.sfs.services;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.EmailAddress;
import com.cargolinklabs.loadmatch.platform.iam.domain.repositories.UserRepository;
import com.cargolinklabs.loadmatch.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User details service implementation
 * <p>
 *     Loads the user details used by Spring Security from the IAM user repository,
 *     using the email address as the username.
 * </p>
 */
@Service(value = "defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by its username (email address)
     *
     * @param username the email address
     * @return the user details
     * @throws UsernameNotFoundException when the email is invalid or no user has it
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var user = userRepository.findByEmail(new EmailAddress(username))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
            return UserDetailsImpl.build(user);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Invalid email: " + username);
        }
    }
}
