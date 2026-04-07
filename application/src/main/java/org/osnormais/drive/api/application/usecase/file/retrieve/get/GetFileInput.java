package org.osnormais.drive.api.application.usecase.file.retrieve.get;

import java.util.UUID;

public record GetFileInput(UUID fileId, UUID actorId) {

}
