package com.cargolinklabs.loadmatch.platform.documents.interfaces.rest;

import com.cargolinklabs.loadmatch.platform.documents.application.commandservices.DocumentCommandService;
import com.cargolinklabs.loadmatch.platform.documents.application.queryservices.DocumentQueryService;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.queries.GetDocumentFileQuery;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.queries.GetDocumentsByCarrierIdQuery;
import com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.resources.UploadDocumentResource;
import com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.transform.DocumentResourceFromEntityAssembler;
import com.cargolinklabs.loadmatch.platform.documents.interfaces.rest.transform.UploadDocumentCommandFromResourceAssembler;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import com.cargolinklabs.loadmatch.platform.shared.interfaces.rest.transform.ProblemDetailFromApplicationErrorAssembler;
import com.cargolinklabs.loadmatch.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentsController {
    private final DocumentCommandService commands;
    private final DocumentQueryService queries;

    public DocumentsController(DocumentCommandService commands, DocumentQueryService queries) {
        this.commands = commands;
        this.queries = queries;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    public ResponseEntity<?> handleMalformedRequest() {
        return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(
                ApplicationError.validationError("document-request", "Invalid or missing document request data"));
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload and validate a carrier document",
            description = "Accepts PDF, JPEG or PNG files up to 5 MB encoded as Base64.")
    public ResponseEntity<?> upload(@Valid @RequestBody UploadDocumentResource resource, Principal principal) {
        var command = UploadDocumentCommandFromResourceAssembler.toCommandFromResource(resource);
        return ResponseEntityAssembler.toResponseEntityFromResult(commands.handle(command, principal.getName()),
                DocumentResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List a carrier's documents and validation results")
    public ResponseEntity<?> getCarrierDocuments(@RequestParam UUID carrierId, Principal principal) {
        var result = queries.handle(new GetDocumentsByCarrierIdQuery(carrierId), principal.getName());
        return ResponseEntityAssembler.toResponseEntityFromResult(result, documents -> documents.stream()
                .map(DocumentResourceFromEntityAssembler::toResourceFromEntity)
                .toList(), HttpStatus.OK);
    }

    @GetMapping("/{documentId}/file")
    @Operation(summary = "Download the uploaded file for a carrier document")
    public ResponseEntity<?> getFile(@PathVariable UUID documentId, Principal principal) {
        var result = queries.handle(new GetDocumentFileQuery(documentId), principal.getName());
        if (result instanceof Result.Failure(var error)) {
            return ProblemDetailFromApplicationErrorAssembler.toResponseFromApplicationError(error);
        }
        var file = result.toOptional().orElseThrow();
        var disposition = ContentDisposition.attachment().filename(file.fileName(), StandardCharsets.UTF_8).build();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .contentLength(file.bytes().length)
                .cacheControl(CacheControl.noStore())
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .header("X-Content-Type-Options", "nosniff")
                .body(file.bytes());
    }
}
