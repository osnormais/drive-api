package org.osnormais.drive.api.application.usecase.transferchannel.retrieve.get;

import static java.util.Objects.requireNonNull;

import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.transferchannel.TransferChannelQueryGateway;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelId;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultGetTransferChannelUseCase extends GetTransferChannelUseCase {

    private final UserQueryGateway userQueryGateway;
    private final TransferChannelQueryGateway transferChannelQueryGateway;

    public DefaultGetTransferChannelUseCase(
            final UserQueryGateway userQueryGateway,
            final TransferChannelQueryGateway transferChannelQueryGateway) {
        this.userQueryGateway = requireNonNull(userQueryGateway);
        this.transferChannelQueryGateway = requireNonNull(transferChannelQueryGateway);
    }

    @Override
    public GetTransferChannelOutput execute(final GetTransferChannelInput input) {

        final UserId actorId = UserId.of(input.actorId());
        final TransferChannelId transferChannelId = TransferChannelId.of(input.id());

        if (!userQueryGateway.existsById(actorId))
            throw NotFoundException.create(User.class, actorId);

        final TransferChannel transferChannel = transferChannelQueryGateway
                .findById(transferChannelId)
                .orElseThrow(() -> NotFoundException.create(TransferChannel.class, transferChannelId));

        return GetTransferChannelOutput.from(transferChannel.ensureBelongsTo(actorId).ensureNotExpired());
    }

}
