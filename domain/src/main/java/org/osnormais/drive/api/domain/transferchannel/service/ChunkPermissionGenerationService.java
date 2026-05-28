package org.osnormais.drive.api.domain.transferchannel.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelType;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkSize;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkSpecification;
import org.osnormais.drive.api.domain.user.UserId;

public final class ChunkPermissionGenerationService {

    public static ChunkPermission generate(
            final TransferChannel transferChannel,
            final File file,
            final UserId actorId,
            final Set<Long> chunkIndexes,
            final Duration validDuration) {

        checkTransferChannel(transferChannel, file, actorId);

        final Long fileSize = file.getSize().bytes();

        final ChunkSpecification chunkSpecification = transferChannel.getChunkSpecification();

        final Set<ChunkPermission.ChunkInfo> chunksInfo = chunkIndexes
                .stream()
                .map(chunkIndex -> new ChunkPermission.ChunkInfo(
                        chunkIndex,
                        chunkSpecification.chunkOffset(fileSize, chunkIndex),
                        chunkSpecification.effectiveChunkSize(fileSize, chunkIndex)))
                .collect(Collectors.toSet());

        return new ChunkPermission(
                actorId,
                transferChannel.getType(),
                file.getId(),
                chunksInfo,
                transferChannel.resolvePermissionExpiresAt(validDuration));

    }

    private static void checkTransferChannel(
            final TransferChannel transferChannel,
            final File file,
            final UserId actorId) {

        transferChannel
                .ensureNotExpired()
                .ensureBelongsTo(actorId)
                .ensureBelongsToFile(file.getId());

    }

    public record ChunkPermission(
            UserId userId,
            TransferChannelType type,
            FileId fileId,
            Set<ChunkInfo> chunks,
            Instant expiresAt) {

        public record ChunkInfo(Long chunkIndex, Long chunkOffset, ChunkSize chunkSize) {
        }

    }

}
