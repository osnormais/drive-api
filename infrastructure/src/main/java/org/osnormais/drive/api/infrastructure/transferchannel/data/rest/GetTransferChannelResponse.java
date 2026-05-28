package org.osnormais.drive.api.infrastructure.transferchannel.data.rest;

import java.time.Instant;
import java.util.UUID;

import org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get.GetTransferChannelOutput;

public record GetTransferChannelResponse(
        UUID id,
        String type,
        Long totalChunks,
        UUID userId,
        UUID fileId,
        Integer maxParallelChunks,
        Long throughputLimit,
        Instant expiresAt) {

    public static GetTransferChannelResponse from(final GetTransferChannelOutput output) {
        return new GetTransferChannelResponse(
                output.id(),
                output.type(),
                output.totalChunks(),
                output.userId(),
                output.fileId(),
                output.maxParallelChunks(),
                output.throughputLimit(),
                output.expiresAt());
    }

}
