package org.osnormais.drive.api.domain.file.service;

import org.osnormais.drive.api.domain.entitlement.quota.Amount;
import org.osnormais.drive.api.domain.entitlement.quota.BytesQuota;
import org.osnormais.drive.api.domain.exception.FileAlreadyExistsException;
import org.osnormais.drive.api.domain.exception.QuotaExceededException;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.valueobject.Checksum;
import org.osnormais.drive.api.domain.file.valueobject.Content;
import org.osnormais.drive.api.domain.file.valueobject.FileName;
import org.osnormais.drive.api.domain.file.valueobject.Size;
import org.osnormais.drive.api.domain.folder.Folder;
import org.osnormais.drive.api.domain.user.User;

public final class FileCreationService {

    public static File createFile(
            final Boolean hasSiblingsWithSameName,
            final BytesQuota remainingQuota,
            final BytesQuota actualUserQuota,
            final User creator,
            final User owner,
            final Folder folder,
            final FileName name,
            final Checksum checksum,
            final Size size,
            final Content content) {

        if (!remainingQuota.isUnlimited() && remainingQuota.amount().orElse(Amount.of(0L)).value() < size.bytes())
            throw QuotaExceededException.with(actualUserQuota);

        if (hasSiblingsWithSameName)
            throw FileAlreadyExistsException.with(name);

        return File.create(
                creator.getId(),
                owner.getId(),
                folder.getId(),
                name,
                checksum,
                size,
                content);
    }

}
