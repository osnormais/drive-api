package org.osnormais.drive.api.infrastructure.transferchannel.data.rest;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.application.usecase.transferchannel.create.CreateTransferChannelOutput;

public record CreateTransferChannelResponse(
        UUID id,
        String type,
        Long totalChunks,
        Long chunkSize,
        UUID userId,
        UUID fileId,
        Integer maxParallelChunks,
        Long throughputLimit,
        Instant expiresAt) {

    public static CreateTransferChannelResponse from(final CreateTransferChannelOutput output) {
        return new CreateTransferChannelResponse(
                output.id(),
                output.type(),
                output.totalChunks(),
                output.chunkSize(),
                output.userId(),
                output.fileId(),
                output.maxParallelChunks(),
                output.throughputLimit(),
                output.expiresAt());
    }

}
