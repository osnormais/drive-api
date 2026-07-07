package org.osnormais.drive.api.infrastructure.folder.data.rest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.osnormais.drive.api.domain.folder.FolderType;

public record GetFolderResponse(
        UUID id,
        UUID parentId,
        FolderType type,
        UUID ownerId,
        String name,
        List<SubFolder> subFolders,
        List<FolderFile> files,
        Instant createdAt,
        Instant updatedAt) {

}
