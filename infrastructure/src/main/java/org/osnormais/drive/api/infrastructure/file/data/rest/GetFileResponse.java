package org.osnormais.drive.api.infrastructure.file.data.rest;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.valueobject.Checksum;

public record GetFileResponse(
        UUID id,
        String name,
        Long sizeInBytes,
        String contentType,
        String checksumValue,
        Checksum.Algorithm checksumAlgorithm,
        UUID folderId,
        UUID ownerId,
        UUID creatorId,
        Instant createdAt,
        Instant updatedAt) {

}
