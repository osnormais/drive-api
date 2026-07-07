package org.osnormais.drive.api.infrastructure.folder.data.rest;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.folder.FolderType;

public record ListFolderItemResponse(
        UUID id,
        String name,
        FolderType type,
        UUID parentId,
        UUID ownerId,
        Instant createdAt,
        Instant updatedAt) {

}
