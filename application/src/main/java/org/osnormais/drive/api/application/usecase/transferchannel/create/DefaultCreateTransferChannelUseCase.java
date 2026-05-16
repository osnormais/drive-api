package org.osnormais.drive.api.application.usecase.transferchannel.create;

import org.osnormais.drive.api.application.exception.NotFoundException;
import org.osnormais.drive.api.application.gateway.user.UserQueryGateway;
import org.osnormais.drive.api.domain.user.User;
import org.osnormais.drive.api.domain.user.UserId;

public class DefaultCreateTransferChannelUseCase extends CreateTransferChannelUseCase {

    private final UserQueryGateway userQueryGateway;

    public DefaultCreateTransferChannelUseCase(
            final UserQueryGateway userQueryGateway) {
        this.userQueryGateway = userQueryGateway;
    }

    @Override
    public CreateTransferChannelOutput execute(final CreateTransferChannelInput input) {

        final UserId userId = UserId.of(input.userId());

        final User user = userQueryGateway
                .findById(userId)
                .orElseThrow(() -> NotFoundException.create(User.class, userId));

        return null;
    }

}
