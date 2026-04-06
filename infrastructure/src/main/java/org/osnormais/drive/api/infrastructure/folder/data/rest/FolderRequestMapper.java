package org.osnormais.drive.api.infrastructure.folder.data.rest;

import java.util.UUID;

import org.osnormais.drive.api.application.usecase.folder.create.CreateFolderInput;
import org.osnormais.drive.api.application.usecase.folder.retrieve.get.root.GetRootFolderInput;

public interface FolderRequestMapper {

    static CreateFolderInput map(final CreateFolderRequest request, final UUID creatorId) {
        return new CreateFolderInput(
                creatorId,
                request.parentFolderId(),
                request.name());
    }

    static GetRootFolderInput map(UUID owner) {
        return new GetRootFolderInput(owner);
    }

}
