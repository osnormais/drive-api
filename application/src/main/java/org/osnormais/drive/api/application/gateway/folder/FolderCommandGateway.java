package org.osnormais.drive.api.application.gateway.folder;

import org.osnormais.drive.api.domain.folder.Folder;

public interface FolderCommandGateway {

    Folder create(Folder folder);

    Folder update(Folder folder);

}
