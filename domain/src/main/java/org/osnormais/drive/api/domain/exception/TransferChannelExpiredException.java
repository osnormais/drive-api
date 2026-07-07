package org.osnormais.drive.api.domain.exception;

public class TransferChannelExpiredException extends SilentDomainException {

    private TransferChannelExpiredException() {
        super("Transfer channel is expired.");
    }

    public static TransferChannelExpiredException create() {
        return new TransferChannelExpiredException();
    }

}
