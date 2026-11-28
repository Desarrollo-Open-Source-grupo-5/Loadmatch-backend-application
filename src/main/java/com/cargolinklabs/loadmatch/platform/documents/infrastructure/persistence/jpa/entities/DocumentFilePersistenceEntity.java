package com.cargolinklabs.loadmatch.platform.documents.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "document_files", schema = "documents")
@Getter
@NoArgsConstructor
public class DocumentFilePersistenceEntity {
    @Id
    private UUID documentId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id")
    private CarrierDocumentPersistenceEntity document;

    @Column(nullable = false, length = 50)
    private String contentType;

    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] content;

    public DocumentFilePersistenceEntity(CarrierDocumentPersistenceEntity document, String contentType, byte[] content) {
        this.document = document;
        this.contentType = contentType;
        this.content = content;
    }
}
