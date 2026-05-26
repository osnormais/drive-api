package org.osnormais.drive.api.domain.transferchannel.service;

import java.time.Duration;

import org.osnormais.drive.api.domain.entitlement.plan.Plan;
import org.osnormais.drive.api.domain.entitlement.quota.Amount;
import org.osnormais.drive.api.domain.entitlement.quota.BandwidthQuota;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.valueobject.FileSize;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelType;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkSize;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkSpecification;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ParallelChunkLimit;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ThroughputLimit;
import org.osnormais.drive.api.domain.user.User;

public final class TransferChannelCreationService {

    public static TransferChannel upload(
            final ThroughputLimit maxRateLimitPerChunk,
            final ParallelChunkLimit maxParallelChunks,
            final BandwidthQuota maxBandwidthQuota,
            final ChunkSize targetChunkSize,
            final Duration validDuration,
            final User user,
            final Plan userPlan,
            final File file) {

        if (file.isPublished())
            throw new IllegalStateException("File is already published and cannot be uploaded again.");

        return create(
                TransferChannelType.UPLOAD,
                maxRateLimitPerChunk,
                maxParallelChunks,
                maxBandwidthQuota,
                targetChunkSize,
                validDuration,
                user,
                userPlan,
                file);

    }

    public static TransferChannel download(
            final ThroughputLimit maxRateLimitPerChunk,
            final ParallelChunkLimit maxParallelChunks,
            final BandwidthQuota maxBandwidthQuota,
            final ChunkSize targetChunkSize,
            final Duration validDuration,
            final User user,
            final Plan userPlan,
            final File file) {

        if (!file.isPublished())
            throw new IllegalStateException("File is not published and cannot be downloaded.");

        return create(
                TransferChannelType.DOWNLOAD,
                maxRateLimitPerChunk,
                maxParallelChunks,
                maxBandwidthQuota,
                targetChunkSize,
                validDuration,
                user,
                userPlan,
                file);

    }

    private static TransferChannel create(
            final TransferChannelType type,
            final ThroughputLimit maxRateLimitPerChunk,
            final ParallelChunkLimit maxParallelChunks,
            final BandwidthQuota maxBandwidthQuota,
            final ChunkSize targetChunkSize,
            final Duration validDuration,
            final User user,
            final Plan userPlan,
            final File file) {

        final BandwidthQuota planBandwidthQuota = userPlan.getBandwidthQuota();

        final BandwidthQuota effectiveTargetBandwidthQuota = planBandwidthQuota.isUnlimited()
                ? maxBandwidthQuota
                : planBandwidthQuota;

        final ChunkSpecification chunkSpecification = createChunkSpecification(
                maxRateLimitPerChunk,
                maxParallelChunks,
                targetChunkSize,
                effectiveTargetBandwidthQuota,
                file.getSize());

        return TransferChannel.create(
                validDuration,
                user.getId(),
                file.getId(),
                type,
                chunkSpecification);

    }

    private static ChunkSpecification createChunkSpecification(
            final ThroughputLimit maxRateLimitPerChunk,
            final ParallelChunkLimit maxParallelChunks,
            final ChunkSize targetChunkSize,
            final BandwidthQuota bandwidthQuota,
            final FileSize fileSize) {

        final Long targetBps = bandwidthQuota.amount().orElse(Amount.zero()).value();
        final Long maxPerChunkBps = maxRateLimitPerChunk.bytesPerSecond();

        final Boolean targetWithinMaxPerChunk = targetBps <= maxPerChunkBps;

        if (targetWithinMaxPerChunk)
            return new ChunkSpecification(
                    ThroughputLimit.create(targetBps),
                    targetChunkSize,
                    ParallelChunkLimit.of(1));

        final Long requiredChunks = targetBps / maxPerChunkBps;
        final Integer boundedChunks = (int) Math.min(maxParallelChunks.value(), requiredChunks);

        return new ChunkSpecification(
                maxRateLimitPerChunk,
                targetChunkSize,
                ParallelChunkLimit.of(boundedChunks));

    }

}
