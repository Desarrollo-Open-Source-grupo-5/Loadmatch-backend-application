package com.cargolinklabs.loadmatch.platform.shared.application.result;

import org.jspecify.annotations.NullMarked;

/**
 * Represents an error that occurred in the application layer.
 * Designed to be easily mapped to HTTP responses and provide structured error information.
 *
 * @param code     A machine-readable error code (e.g., "PROFILE_ALREADY_EXISTS", "CARRIER_NOT_FOUND")
 * @param message  A human-readable error message
 * @param details  Optional additional context about the error
 */

@NullMarked
public record ApplicationError(
        String code,
        String message,
        String details
) {

    public ApplicationError(String code, String message) {
        this(code, message, null);
    }

    /**
     * Validation error: input data is invalid or violates constraints CL
     */
    public static ApplicationError validationError(String fieldOrConcept, String reason) {
        return new ApplicationError(
                "VALIDATION_ERROR",
                "Validation failed: %s".formatted(fieldOrConcept),
                reason);
    }

    /**
     * Not found error: the requested resource does not exist AU
     */
    public static ApplicationError notFound(String resourceType, String identifier) {
        return new ApplicationError(
                "%s_NOT_FOUND".formatted(resourceType.toUpperCase()),
                "%s not found: %s".formatted(resourceType, identifier),
                null);
    }

    /**
     * Business rule violation: operation violates domain constraints DE
     */
    public static ApplicationError businessRuleViolation(String rule, String reason) {
        return new ApplicationError(
                "BUSINESS_RULE_VIOLATION",
                "Business rule violation: %s".formatted(rule),
                reason);
    }

    /**
     * Conflict error: operation cannot be completed due to conflicting state AI
     */
    public static ApplicationError conflict(String resource, String reason) {
        return new ApplicationError(
                "%s_CONFLICT".formatted(resource.toUpperCase()),
                "Conflict with %s".formatted(resource),
                reason);
    }

    /**
     * Unexpected error: something went wrong that shouldn't have USED
     */
    public static ApplicationError unexpected(String context, String reason) {
        return new ApplicationError(
                "UNEXPECTED_ERROR",
                "Unexpected error in %s".formatted(context),
                reason);
    }
}
