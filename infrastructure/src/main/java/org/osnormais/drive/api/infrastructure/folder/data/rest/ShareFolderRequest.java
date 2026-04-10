package org.osnormais.drive.api.infrastructure.folder.data.rest;

import java.util.UUID;

import org.osnormais.drive.api.domain.acl.Permission;

public record ShareFolderRequest(UUID userId, Permission permission) {

}
