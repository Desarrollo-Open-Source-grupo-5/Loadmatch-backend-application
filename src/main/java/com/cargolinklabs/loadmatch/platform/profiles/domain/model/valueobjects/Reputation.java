package com.cargolinklabs.loadmatch.platform.profiles.domain.model.valueobjects;

/**
 * Reputation value object with average score and total ratings count
 */
public record Reputation(double average, int totalRatings) {

    public Reputation {
        if (average < 0.0 || average > 5.0) {
            throw new IllegalArgumentException("Average reputation must be between 0.0 and 5.0");
        }
        if (totalRatings < 0) {
            throw new IllegalArgumentException("Total ratings cannot be negative");
        }
    }

    public static Reputation initial() {
        return new Reputation(0.0, 0);
    }

    //add a new score and recalculate average incrementally
    public Reputation addScore(int score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }
        var nextTotal = this.totalRatings + 1;
        var nextAverage = ((this.average * this.totalRatings) + score) / nextTotal;
        var rounded = Math.round(nextAverage * 100.0) / 100.0;
        return new Reputation(rounded, nextTotal);
    }
}

