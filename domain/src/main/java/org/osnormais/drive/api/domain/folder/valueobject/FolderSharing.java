package org.osnormais.drive.api.domain.folder.valueobject;

import static java.util.Objects.isNull;

import java.time.Instant;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record FolderSharing(
        UserId sharedTo,
        UserId sharedBy,
        FolderId virtuaFolder,
        Instant sharedAt) implements ValueObject {

    public static FolderSharing create(final UserId sharedTo, final UserId sharedBy, final FolderId virtuaFolder) {
        return new FolderSharing(sharedTo, sharedBy, virtuaFolder, Instant.now());
    }

    public static FolderSharing with(
            final UserId sharedTo,
            final UserId sharedBy,
            final FolderId virtuaFolder,
            final Instant sharedAt) {
        return new FolderSharing(sharedTo, sharedBy, virtuaFolder, sharedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(sharedTo))
            handler.append(ValidationError.with("Shared to user is required"));
        else
            sharedTo.validate(handler);

        if (isNull(sharedBy))
            handler.append(ValidationError.with("Shared by user is required"));
        else
            sharedBy.validate(handler);

        if (isNull(virtuaFolder))
            handler.append(ValidationError.with("Virtual folder is required"));
        else
            virtuaFolder.validate(handler);

        if (isNull(sharedAt))
            handler.append(ValidationError.with("Shared at timestamp is required"));

    }

}
