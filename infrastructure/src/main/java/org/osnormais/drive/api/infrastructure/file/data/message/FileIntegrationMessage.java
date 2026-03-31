package org.osnormais.drive.api.infrastructure.file.data.message;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;

public record FileIntegrationMessage(
        UUID id,
        UUID creatorId,
        UUID ownerId,
        UUID folderId,
        Checksum.Algorithm checksumAlgorithm,
        String checksumValue,
        Long size,
        String name,
        String contentType,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt) implements Serializable {

    public static FileIntegrationMessage of(final File file) {
        return new FileIntegrationMessage(
                file.getId().getValue(),
                file.getCreator().getValue(),
                file.getOwner().getValue(),
                file.getFolder().getValue(),
                file.getChecksum().algorithm(),
                file.getChecksum().value(),
                file.getSize().bytes(),
                file.getName().value(),
                file.getContent().type(),
                file.getCreatedAt(),
                file.getUpdatedAt(),
                file.getDeletedAt());
    }

}
