package org.osnormais.drive.api.domain.exception;

import org.osnormais.drive.api.domain.user.UserId;

public class TransferChannelNotOwnedByUserException extends SilentDomainException {

    private static final String MESSAGE_TEMPLATE = "Transfer channel does not belong to user [%s]";

    private TransferChannelNotOwnedByUserException(final UserId userId) {
        super(formatMessage(userId));
    }

    public static TransferChannelNotOwnedByUserException with(final UserId userId) {
        return new TransferChannelNotOwnedByUserException(userId);
    }

    private static String formatMessage(final UserId userId) {
        return MESSAGE_TEMPLATE.formatted(userId.getStringValue());
    }

}
