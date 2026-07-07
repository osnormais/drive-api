package org.osnormais.drive.api.infrastructure.file.data.rest;

import java.time.Instant;
import java.util.UUID;

public record ListFileItemResponse(
        UUID id,
        String name,
        Long sizeInBytes,
        String contentType,
        UUID folderId,
        UUID ownerId,
        UUID creatorId,
        Instant createdAt,
        Instant updatedAt) {

}
