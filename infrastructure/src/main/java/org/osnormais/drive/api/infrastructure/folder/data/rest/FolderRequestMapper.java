package org.osnormais.drive.api.infrastructure.folder.data.rest;

import java.util.UUID;

import org.osnormais.drive.api.application.usecase.folder.create.CreateFolderInput;
import org.osnormais.drive.api.application.usecase.folder.sharings.create.ShareFolderInput;

public interface FolderRequestMapper {

    static CreateFolderInput map(final CreateFolderRequest request, final UUID creatorId) {
        return new CreateFolderInput(
                creatorId,
                request.parentFolderId(),
                request.name());
    }

    static ShareFolderInput map(final ShareFolderRequest request, final UUID ownerId, final UUID folderId) {
        return new ShareFolderInput(
                folderId,
                request.userId(),
                ownerId,
                request.permission(),
                request.expiresAt());
    }

}
