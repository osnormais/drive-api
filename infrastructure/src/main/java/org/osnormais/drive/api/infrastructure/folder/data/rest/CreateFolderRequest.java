package org.osnormais.drive.api.infrastructure.folder.data.rest;

import java.util.UUID;

public record CreateFolderRequest(
        UUID parentFolderId,
        String name) {

}
