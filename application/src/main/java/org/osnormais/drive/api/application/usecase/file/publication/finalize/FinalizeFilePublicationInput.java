package org.osnormais.drive.api.application.usecase.file.publication.finalize;

import java.util.Optional;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.valueobject.Publication;

public record FinalizeFilePublicationInput(UUID fileId, Publication.Status status, Optional<String> errorMessage) {

}
