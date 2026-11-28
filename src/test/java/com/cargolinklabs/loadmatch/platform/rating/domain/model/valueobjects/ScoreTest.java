package com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    void constructorWithValidScoreCreatesInstance() {
        var score = new Score(5);

        assertEquals(5, score.value());
    }

    @Test
    void constructorWithScoreBelowMinimumThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Score(0));
    }

    @Test
    void constructorWithScoreAboveMaximumThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Score(6));
    }
}

