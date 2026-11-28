package com.cargolinklabs.loadmatch.platform.matching.domain.model.valueobjects;

import com.cargolinklabs.loadmatch.platform.shared.domain.model.valueobjects.Coordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchCriteriaTest {

    @Test
    void constructorWithValidValuesCreatesCriteria() {
        var origin = new Coordinates(-12.0464, -77.0428);
        var criteria = new SearchCriteria(origin, 50.0);
        assertEquals(50.0, criteria.radiusKm());
        assertEquals(origin, criteria.origin());
    }

    @Test
    void constructorWithNullOriginThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new SearchCriteria(null, 30.0));
    }

    @Test
    void constructorWithOutOfRangeRadiusThrowsException() {
        var origin = new Coordinates(-12.0464, -77.0428);
        assertThrows(IllegalArgumentException.class, () -> new SearchCriteria(origin, 0));
        assertThrows(IllegalArgumentException.class, () -> new SearchCriteria(origin, 501));
    }
}
