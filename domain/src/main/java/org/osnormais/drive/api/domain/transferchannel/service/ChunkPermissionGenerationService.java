package org.osnormais.drive.api.domain.transferchannel.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkSize;
import org.osnormais.drive.api.domain.user.UserId;

public final class ChunkPermissionGenerationService {

    public static ChunkPermission generate(
            final TransferChannel transferChannel,
            final File file,
            final UserId actorId,
            final Set<Long> chunkIndexes,
            final Duration validDuration) {

        transferChannel
                .ensureNotExpired()
                .ensureBelongsTo(actorId)
                .ensureBelongsToFile(file.getId());

        final Long fileSize = file.getSize().bytes();

        final Set<ChunkPermission.ChunkInfo> chunksInfo = chunkIndexes
                .stream()
                .map(chunkIndex -> new ChunkPermission.ChunkInfo(
                        chunkIndex,
                        transferChannel
                                .getChunkSpecification()
                                .effectiveChunkSize(fileSize, chunkIndex)))
                .collect(Collectors.toSet());

        return new ChunkPermission(chunksInfo, transferChannel.resolvePermissionExpiresAt(validDuration));

    }

    public record ChunkPermission(Set<ChunkInfo> chunks, Instant expiresAt) {

        public record ChunkInfo(Long chunkIndex, ChunkSize chunkSize) {
        }

    }

}
