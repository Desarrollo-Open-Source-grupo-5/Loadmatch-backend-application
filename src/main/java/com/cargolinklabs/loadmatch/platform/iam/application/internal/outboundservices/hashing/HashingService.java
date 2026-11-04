package com.cargolinklabs.loadmatch.platform.iam.application.internal.outboundservices.hashing;

/**
 * Hashing service port used to protect user passwords
 */
public interface HashingService {
    /**
     * Hashes a raw password
     * @param rawPassword the raw password
     * @return the hashed password
     */
    String encode(CharSequence rawPassword);

    /**
     * Checks a raw password against a hashed password
     * @param rawPassword the raw password
     * @param encodedPassword the hashed password
     * @return true when both match
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
