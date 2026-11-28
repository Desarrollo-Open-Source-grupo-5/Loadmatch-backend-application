package com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects;

/**
 * Optional rating comment value object
 */
public record Comment(String text) {

    public Comment {
        if (text != null) {
            text = text.trim();
            if (text.length() > 500) {
                throw new IllegalArgumentException("Comment cannot exceed 500 characters");
            }
        }
    }
}

