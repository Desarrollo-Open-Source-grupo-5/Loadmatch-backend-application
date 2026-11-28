package com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects;

/**
 * Score value object from 1 to 5 stars
 */
public record Score(int value) {

    public Score {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }
    }
}

