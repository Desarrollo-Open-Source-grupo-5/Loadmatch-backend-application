package com.cargolinklabs.loadmatch.platform.documents.application.internal.queryservices;

import com.cargolinklabs.loadmatch.platform.documents.application.internal.outboundservices.acl.DocumentAccessService;
import com.cargolinklabs.loadmatch.platform.documents.application.queryservices.DocumentQueryService;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.CarrierDocument;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.DocumentType;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.queries.GetDocumentFileQuery;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.queries.GetDocumentTypesQuery;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.queries.GetDocumentsByCarrierIdQuery;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentFile;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentId;
import com.cargolinklabs.loadmatch.platform.documents.domain.repositories.DocumentRepository;
import com.cargolinklabs.loadmatch.platform.documents.domain.repositories.DocumentTypeRepository;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DocumentQueryServiceImpl implements DocumentQueryService {
    private final DocumentTypeRepository documentTypes;
    private final DocumentRepository documents;
    private final DocumentAccessService access;

    public DocumentQueryServiceImpl(DocumentTypeRepository documentTypes, DocumentRepository documents,
                                    DocumentAccessService access) {
        this.documentTypes = documentTypes;
        this.documents = documents;
        this.access = access;
    }

    @Override
    public List<DocumentType> handle(GetDocumentTypesQuery query) {
        return documentTypes.findAll();
    }

    @Override
    public Result<List<CarrierDocument>, ApplicationError> handle(GetDocumentsByCarrierIdQuery query, String email) {
        if (!access.isOwner(query.carrierId(), email)) {
            return Result.failure(ApplicationError.notFound("carrier", query.carrierId().toString()));
        }
        return Result.success(documents.findByCarrierId(query.carrierId()));
    }

    @Override
    public Result<DocumentFile, ApplicationError> handle(GetDocumentFileQuery query, String email) {
        var id = new DocumentId(query.documentId());
        return documents.findById(id)
                .filter(document -> access.isOwner(document.getCarrierId(), email))
                .flatMap(document -> documents.findFileById(id))
                .<Result<DocumentFile, ApplicationError>>map(Result::success)
                .orElseGet(() -> Result.failure(ApplicationError.notFound("document", query.documentId().toString())));
    }
}
