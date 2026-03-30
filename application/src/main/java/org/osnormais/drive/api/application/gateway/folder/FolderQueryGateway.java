package org.osnormais.drive.api.application.gateway.folder;

import java.util.Optional;

import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;

public interface FolderQueryGateway {

    Optional<Folder> findVisibleById(FolderId id, UserId userId);

}