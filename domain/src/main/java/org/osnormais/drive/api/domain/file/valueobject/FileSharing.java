package org.osnormais.drive.api.domain.file.valueobject;

import static java.util.Objects.isNull;

import java.time.Instant;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.folder.FolderId;
import org.osnormais.drive.api.domain.user.UserId;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record FileSharing(
        UserId sharedTo,
        UserId sharedBy,
        FolderId virtualFolder,
        Instant sharedAt) implements ValueObject {

    public static FileSharing create(final UserId sharedTo, final UserId sharedBy, final FolderId virtualFolder) {
        return new FileSharing(sharedTo, sharedBy, virtualFolder, Instant.now());
    }

    public static FileSharing with(
            final UserId sharedTo,
            final UserId sharedBy,
            final FolderId virtualFolder,
            final Instant sharedAt) {
        return new FileSharing(sharedTo, sharedBy, virtualFolder, sharedAt);
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

        if (isNull(virtualFolder))
            handler.append(ValidationError.with("Virtual folder is required"));
        else
            virtualFolder.validate(handler);

        if (isNull(sharedAt))
            handler.append(ValidationError.with("Shared at timestamp is required"));

    }

}
