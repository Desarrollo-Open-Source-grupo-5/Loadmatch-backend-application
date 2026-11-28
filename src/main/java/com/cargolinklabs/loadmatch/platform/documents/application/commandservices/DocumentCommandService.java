package com.cargolinklabs.loadmatch.platform.documents.application.commandservices;

import com.cargolinklabs.loadmatch.platform.documents.domain.model.aggregates.CarrierDocument;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.commands.SeedDocumentTypesCommand;
import com.cargolinklabs.loadmatch.platform.documents.domain.model.commands.UploadDocumentCommand;
import com.cargolinklabs.loadmatch.platform.shared.application.result.ApplicationError;
import com.cargolinklabs.loadmatch.platform.shared.application.result.Result;

public interface DocumentCommandService {
    Result<CarrierDocument, ApplicationError> handle(UploadDocumentCommand command, String email);

    void handle(SeedDocumentTypesCommand command);
}
