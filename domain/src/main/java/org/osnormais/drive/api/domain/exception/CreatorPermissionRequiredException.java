package org.osnormais.drive.api.domain.exception;

import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.user.UserId;

public class CreatorPermissionRequiredException extends SilentDomainException {

    private static final String MESSAGE_TEMPLATE = "User [%s] must be the creator of file [%s] to initiate publication";

    private CreatorPermissionRequiredException(
            final UserId user,
            final FileId fileId) {
        super(formatMessage(user, fileId));
    }

    public static CreatorPermissionRequiredException with(
            final UserId user,
            final FileId fileId) {
        return new CreatorPermissionRequiredException(user, fileId);
    }

    private static String formatMessage(
            final UserId user,
            final FileId fileId) {

        return MESSAGE_TEMPLATE.formatted(
                user.getStringValue(),
                fileId.getStringValue());
    }

}
