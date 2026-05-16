package org.osnormais.drive.api.application.usecase.transferchannel.create;

import static java.util.Objects.requireNonNull;

import java.time.Duration;

import org.osnormais.drive.api.application.common.annotation.Transactional;
import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.entitlement.plan.PlanQueryGateway;
import org.osnormais.drive.api.application.gateway.file.FileQueryGateway;
import org.osnormais.drive.api.application.gateway.transferchannel.TransferChannelCommandGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.entitlement.plan.Plan;
import org.osnormais.drive.api.domain.entitlement.quota.Amount;
import org.osnormais.drive.api.domain.entitlement.quota.BandwidthQuota;
import org.osnormais.drive.api.domain.file.File;
import org.osnormais.drive.api.domain.file.FileId;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.service.TransferChannelCreationService;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultCreateTransferChannelUseCase extends CreateTransferChannelUseCase {

    private final BandwidthQuota maxBandwidthQuota;
    private final Duration validDuration;

    private final UserQueryGateway userQueryGateway;
    private final FileQueryGateway fileQueryGateway;
    private final PlanQueryGateway planQueryGateway;
    private final TransferChannelCommandGateway transferChannelCommandGateway;

    public DefaultCreateTransferChannelUseCase(
            final Long maxBandwidthQuotaBytesPerSecond,
            final Long validDurationSeconds,
            final UserQueryGateway userQueryGateway,
            final FileQueryGateway fileQueryGateway,
            final PlanQueryGateway planQueryGateway,
            final TransferChannelCommandGateway transferChannelCommandGateway) {
        this.maxBandwidthQuota = BandwidthQuota.of(Amount.of(requireNonNull(maxBandwidthQuotaBytesPerSecond)));
        this.validDuration = Duration.ofSeconds(requireNonNull(validDurationSeconds));
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.fileQueryGateway = requireNonNull(fileQueryGateway);
        this.planQueryGateway = requireNonNull(planQueryGateway);
        this.transferChannelCommandGateway = requireNonNull(transferChannelCommandGateway);
    }

    @Override
    @Transactional
    public CreateTransferChannelOutput execute(final CreateTransferChannelInput input) {

        final UserId userId = UserId.of(input.userId());
        final FileId fileId = FileId.of(input.fileId());

        final User user = userQueryGateway
                .findById(userId)
                .orElseThrow(() -> NotFoundException.create(User.class, userId));

        final File file = fileQueryGateway
                .findVisibleById(fileId, userId)
                .orElseThrow(() -> NotFoundException.create(File.class, fileId));

        final Plan userPlan = planQueryGateway
                .findById(user.getPlan())
                .orElseThrow(() -> NotFoundException.create(Plan.class, user.getPlan()));

        final TransferChannel transferChannel = TransferChannelCreationService.upload(
                validDuration,
                user,
                userPlan,
                file,
                maxBandwidthQuota);

        return CreateTransferChannelOutput.from(transferChannelCommandGateway.create(transferChannel));

    }

}
