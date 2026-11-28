package com.cargolinklabs.loadmatch.platform.documents.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.documents.application.queryservices.DocumentQueryService;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.queries.GetDocumentTypesQuery;
import com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.resources.DocumentTypeResource;
import com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.transform.DocumentTypeResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document-types")
public class DocumentTypesController {
    private final DocumentQueryService queries;

    public DocumentTypesController(DocumentQueryService queries) {
        this.queries = queries;
    }

    @GetMapping
    @Operation(summary = "List the available carrier document types")
    public List<DocumentTypeResource> getDocumentTypes() {
        return queries.handle(new GetDocumentTypesQuery()).stream()
                .map(DocumentTypeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
    }
}
