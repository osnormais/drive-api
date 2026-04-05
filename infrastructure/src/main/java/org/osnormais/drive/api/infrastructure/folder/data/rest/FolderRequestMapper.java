package org.osnormais.drive.api.infrastructure.folder.data.rest;

import java.util.UUID;

import org.osnormais.drive.api.application.usecase.folder.create.CreateFolderInput;

public interface FolderRequestMapper {

    static CreateFolderInput map(final CreateFolderRequest request, final UUID creatorId) {
        return new CreateFolderInput(
                creatorId,
                request.parentFolderId(),
                request.name());
    }

}
