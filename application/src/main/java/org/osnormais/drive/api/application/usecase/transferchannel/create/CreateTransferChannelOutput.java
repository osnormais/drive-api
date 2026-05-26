package org.osnormais.drive.api.application.usecase.transferchannel.create;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.transferchannel.TransferChannel;

public record CreateTransferChannelOutput(
        UUID id,
        UUID userId,
        UUID fileId,
        Instant expiresAt) {

    public static CreateTransferChannelOutput from(final TransferChannel transferChannel) {
        return new CreateTransferChannelOutput(
                transferChannel.getId().getValue(),
                transferChannel.getUser().getValue(),
                transferChannel.getFile().getValue(),
                transferChannel.getExpiresAt());
    }

}
