package org.osnormais.drive.api.domain.folder.service;

import org.osnormais.drive.api.domain.exception.FolderAlreadyExistsException;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.valueobject.FolderName;
import org.osnormais.drive.api.domain.user.User;

public final class FolderCreationService {

    private FolderCreationService() {
    }

    public static Folder createFolder(
            final Boolean hasSiblingsWithSameName,
            final User creator,
            final Folder parentFolder,
            final FolderName name) {

        if (hasSiblingsWithSameName)
            throw FolderAlreadyExistsException.with(name);

        return Folder.create(
                creator.getId(),
                parentFolder,
                name);

    }

}
