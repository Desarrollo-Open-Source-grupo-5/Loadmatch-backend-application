package com.cargolinklabs.loadmatch.platform.documents.application.queryservices;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.CarrierDocument;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.DocumentType;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.queries.GetDocumentFileQuery;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.queries.GetDocumentTypesQuery;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.queries.GetDocumentsByCarrierIdQuery;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.valueobjects.DocumentFile;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

import java.util.List;

public interface DocumentQueryService {
    List<DocumentType> handle(GetDocumentTypesQuery query);

    Result<List<CarrierDocument>, ApplicationError> handle(GetDocumentsByCarrierIdQuery query, String email);

    Result<DocumentFile, ApplicationError> handle(GetDocumentFileQuery query, String email);
}
