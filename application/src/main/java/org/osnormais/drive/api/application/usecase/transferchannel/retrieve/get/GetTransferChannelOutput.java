package org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.domain.file.valueobject.FileSize;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;

public record GetTransferChannelOutput(
        UUID id,
        String type,
        Long totalChunks,
        Long chunkSize,
        UUID userId,
        UUID fileId,
        Integer maxParallelChunks,
        Long throughputLimit,
        Instant expiresAt) {

    public static GetTransferChannelOutput from(final TransferChannel transferChannel, final FileSize fileSize) {
        return new GetTransferChannelOutput(
                transferChannel.getId().getValue(),
                transferChannel.getType().name(),
                transferChannel.getChunkSpecification().totalChunks(fileSize.bytes()),
                transferChannel.getChunkSpecification().chunkSize().bytes(),
                transferChannel.getUser().getValue(),
                transferChannel.getFile().getValue(),
                transferChannel.getChunkSpecification().parallelChunkLimit().value(),
                transferChannel.getChunkSpecification().throughputLimit().bytesPerSecond(),
                transferChannel.getExpiresAt());
    }

}
