package org.osnormais.drive.api.domain.exception;

import java.util.List;

import org.osnormais.drive.api.domain.folder.valueobject.FolderName;

public class FolderAlreadyExistsException extends SilentDomainException {

    private FolderAlreadyExistsException(final FolderName folderName) {
        super(
                "Folder name [" + folderName.value() + "] already exists.",
                List.of(DomainException.Error.with(
                        "A folder with the same name already exists in the target folder. Please choose a different name or remove the existing folder.")));
    }

    public static FolderAlreadyExistsException with(final FolderName folderName) {
        return new FolderAlreadyExistsException(folderName);
    }
}
