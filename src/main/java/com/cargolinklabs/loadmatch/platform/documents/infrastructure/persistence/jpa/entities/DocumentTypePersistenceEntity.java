package com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "document_types", schema = "documents")
@Getter
@NoArgsConstructor
public class DocumentTypePersistenceEntity {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 40)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private boolean requiresExpiration;

    @Column(nullable = false)
    private boolean mandatory;
}
