package org.osnormais.drive.api.application.usecase.folder.create;

import java.util.UUID;

public record CreateFolderInput(
        UUID creatorId,
        UUID parentFolderId,
        String name) {

}
