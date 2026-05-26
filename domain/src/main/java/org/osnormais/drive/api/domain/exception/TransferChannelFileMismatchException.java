package org.osnormais.drive.api.domain.exception;

import org.osnormais.drive.api.domain.file.FileId;

public class TransferChannelFileMismatchException extends SilentDomainException {

    private static final String MESSAGE_TEMPLATE = "Transfer channel does not belong to file [%s]";

    private TransferChannelFileMismatchException(final FileId fileId) {
        super(formatMessage(fileId));
    }

    public static TransferChannelFileMismatchException with(final FileId fileId) {
        return new TransferChannelFileMismatchException(fileId);
    }

    private static String formatMessage(final FileId fileId) {
        return MESSAGE_TEMPLATE.formatted(fileId.getStringValue());
    }

}
