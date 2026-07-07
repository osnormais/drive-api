package org.osnormais.drive.api.infrastructure.folder.data.rest;

import java.time.Instant;
import java.util.UUID;

public record FolderFile(
        UUID id,
        String name,
        Long sizeInBytes,
        String contentType,
        Instant createdAt) {

}
