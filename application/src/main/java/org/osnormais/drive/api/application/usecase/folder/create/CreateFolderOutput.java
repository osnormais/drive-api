package org.osnormais.drive.api.application.usecase.folder.create;

import java.util.UUID;

import org.osnormais.drive.api.domain.folder.Folder;

public record CreateFolderOutput(UUID id) {

    public static CreateFolderOutput from(final Folder folder) {
        return new CreateFolderOutput(folder.getId().getValue());
    }

}
