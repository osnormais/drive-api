package org.osnormais.drive.api.infrastructure.file.data.message.integration.storage;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.file.valueobject.Publication;

public record StorageFileIntegrationMessage(
        UUID id,
        Long sizeInBytes,
        Checksum.Algorithm checksumAlgorithm,
        String checksumValue,
        Instant publishedAt,
        Publication.Status publicationStatus,
        String publicationError) implements Serializable {

}
