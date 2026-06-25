package org.osnormais.drive.api.application.usecase.file.publication.init;

import java.util.UUID;

public record InitFilePublicationInput(UUID fileId, UUID actorId) {

}
