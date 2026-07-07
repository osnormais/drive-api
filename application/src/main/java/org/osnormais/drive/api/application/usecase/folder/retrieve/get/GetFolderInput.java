package org.osnormais.drive.api.application.usecase.folder.retrieve.get;

import java.util.UUID;

public record GetFolderInput(UUID folderId, UUID userId) {

}
