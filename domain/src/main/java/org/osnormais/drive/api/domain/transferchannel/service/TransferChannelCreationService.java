package org.osnormais.drive.api.domain.transferchannel.service;

import java.time.Duration;

import org.osnormais.drive.api.domain.entitlement.plan.Plan;
import org.osnormais.drive.api.domain.entitlement.quota.Amount;
import org.osnormais.drive.api.domain.entitlement.quota.BandwidthQuota;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelType;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ChunkSpecification;
import org.osnormais.drive.api.domain.transferchannel.valueobject.ThroughputLimit;
import org.osnormais.drive.api.domain.user.User;

public final class TransferChannelCreationService {

    public static TransferChannel upload(
            final Duration validDuration,
            final User user,
            final Plan userPlan,
            final File file,
            final BandwidthQuota maxBandwidthQuota) {

        final BandwidthQuota planBandwidthQuota = userPlan.getBandwidthQuota();

        final BandwidthQuota effectiveTargetBandwidthQuota = planBandwidthQuota.isUnlimited()
                ? maxBandwidthQuota
                : planBandwidthQuota;

        final ChunkSpecification chunkSpecification = ChunkSpecification
                .create(ThroughputLimit.create(effectiveTargetBandwidthQuota.amount().orElse(Amount.zero()).value()));

        return TransferChannel.create(
                validDuration,
                user.getId(),
                file.getId(),
                TransferChannelType.UPLOAD,
                chunkSpecification);

    }

}
