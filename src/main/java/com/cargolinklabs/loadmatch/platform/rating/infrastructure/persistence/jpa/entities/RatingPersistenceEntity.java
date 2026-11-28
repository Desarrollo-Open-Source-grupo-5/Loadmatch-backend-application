package com.cargolinklabs.loadmatch.platform.rating.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.rating.domain.model.valueobjects.RaterType;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Rating persistence entity stored in the rating schema
 */
@Entity
@Table(
        name = "ratings",
        schema = "rating",
        uniqueConstraints = @UniqueConstraint(name = "uk_ratings_trip_rater", columnNames = {"trip_id", "rater_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class RatingPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "trip_id", nullable = false)
    private UUID tripId;

    @Column(name = "rater_id", nullable = false)
    private UUID raterId;

    @Column(name = "ratee_id", nullable = false)
    private UUID rateeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rater_type", nullable = false, length = 20)
    private RaterType raterType;

    @Column(nullable = false)
    private int score;

    @Column(length = 500)
    private String comment;
}

