package org.osnormais.drive.api.application.usecase.file.retrieve.get;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.user.UserId;

public record GetFileOutput(
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

    public static GetFileOutput from(final File file, final UserId actor) {
        return new GetFileOutput(
                file.getId().getValue(),
                file.getName().value(),
                file.getSize().bytes(),
                file.getContent().type(),
                file.getChecksum().value(),
                file.getChecksum().algorithm(),
                file.getFolderFor(actor).getValue(),
                file.getOwner().getValue(),
                file.getCreator().getValue(),
                file.getCreatedAt(),
                file.getUpdatedAt());
    }

}
