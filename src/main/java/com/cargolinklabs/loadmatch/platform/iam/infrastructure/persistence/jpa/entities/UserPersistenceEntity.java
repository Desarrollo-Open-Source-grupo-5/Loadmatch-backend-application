package com.cargolinklabs.loadmatch.platform.iam.infrastructure.persistence.jpa.entities;

import com.cargolinklabs.loadmatch.platform.iam.domain.model.valueobjects.Roles;
import com.cargolinklabs.loadmatch.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * User persistence entity stored in the iam schema
 */
@Entity
@Table(name = "users", schema = "iam")
@Getter
@Setter
@NoArgsConstructor
public class UserPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Roles role;

    @Column(nullable = false)
    private boolean active;

    private LocalDateTime lastAccessAt;
}
