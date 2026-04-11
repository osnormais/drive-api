package org.osnormais.drive.api.application.usecase.folder.sharings.create;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.acl.Permission;

public record ShareFolderInput(
        UUID folderId,
        UUID sharedTo,
        UUID sharedBy,
        Permission permission,
        Instant expiresAt) {

}
