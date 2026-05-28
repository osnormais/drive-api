package org.osnormais.drive.api.application.usecase.transferchannel.retrieve.chunk.permission;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.osnormais.drive.api.domain.transferchannel.service.ChunkPermissionGenerationService.ChunkPermission;

public record GetTransferChannelPermissionOutput(
        UUID actorId,
        UUID fileId,
        String type,
        Instant expiresAt,
        Set<ChunkInfo> chunks) {

    public static GetTransferChannelPermissionOutput from(final ChunkPermission chunkPermission) {
        return new GetTransferChannelPermissionOutput(
                chunkPermission.userId().getValue(),
                chunkPermission.fileId().getValue(),
                chunkPermission.type().name(),
                chunkPermission.expiresAt(),
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
