package org.osnormais.drive.api.application.usecase.transferchannel.create;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.valueobject.FileSize;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;

public record CreateTransferChannelOutput(UUID id, Long totalChunks, Instant expiresAt) {

    public static CreateTransferChannelOutput from(final TransferChannel transferChannel, final FileSize fileSize) {
        return new CreateTransferChannelOutput(
                transferChannel.getId().getValue(),
                transferChannel.getChunkSpecification().totalChunks(fileSize.bytes()),
                transferChannel.getExpiresAt());
    }

}
