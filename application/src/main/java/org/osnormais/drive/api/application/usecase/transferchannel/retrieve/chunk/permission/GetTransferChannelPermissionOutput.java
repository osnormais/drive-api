package org.osnormais.drive.api.application.usecase.transferchannel.retrieve.chunk.permission;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.service.ChunkPermissionGenerationService.ChunkPermission;

public record GetTransferChannelPermissionOutput(
        UUID actorId,
        UUID fileId,
        String type,
        Instant expiresAt,
        Integer maxParallelChunks,
        Long throughputLimit,
        Set<ChunkInfo> chunks) {

    public static GetTransferChannelPermissionOutput from(
            final TransferChannel transferChannel,
            final ChunkPermission chunkPermission) {
        return new GetTransferChannelPermissionOutput(
                chunkPermission.userId().getValue(),
                chunkPermission.fileId().getValue(),
                chunkPermission.type().name(),
                chunkPermission.expiresAt(),
                transferChannel.getChunkSpecification().parallelChunkLimit().value(),
                transferChannel.getChunkSpecification().throughputLimit().bytesPerSecond(),
                chunkPermission.chunks().stream().map(ChunkInfo::from).collect(Collectors.toSet()));
    }

    public record ChunkInfo(Long chunkIndex, Long chunkOffset, Long chunkSize) {

        public static ChunkInfo from(final ChunkPermission.ChunkInfo chunkInfo) {
            return new ChunkInfo(
                    chunkInfo.chunkIndex(),
                    chunkInfo.chunkOffset(),
                    chunkInfo.chunkSize().bytes());
        }

    }

}
