package org.osnormais.drive.api.application.usecase.transferchannel.create;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.valueobject.FileSize;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;

public record CreateTransferChannelOutput(
        UUID id,
        String type,
        Long totalChunks,
        UUID userId,
        UUID fileId,
        Instant expiresAt) {

    public static CreateTransferChannelOutput from(final TransferChannel transferChannel, final FileSize fileSize) {
        return new CreateTransferChannelOutput(
                transferChannel.getId().getValue(),
                transferChannel.getType().name(),
                transferChannel.getChunkSpecification().totalChunks(fileSize.bytes()),
                transferChannel.getUser().getValue(),
                transferChannel.getFile().getValue(),
                transferChannel.getExpiresAt());
    }

}
