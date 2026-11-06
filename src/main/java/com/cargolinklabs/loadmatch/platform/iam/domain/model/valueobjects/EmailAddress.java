package com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

/**
 * Email address value object used as the user credential
 *
 * @param address the email address, stored in lower case
 */
public record EmailAddress(String address) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    public EmailAddress {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Email address cannot be null or blank");
        }
        address = address.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("Email address has an invalid format");
        }
    }
}
