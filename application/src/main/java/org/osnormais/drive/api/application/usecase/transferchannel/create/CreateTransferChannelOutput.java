package org.osnormais.drive.api.application.usecase.transferchannel.create;

import java.util.UUID;

import org.osnormais.drive.api.domain.transferchannel.TransferChannel;

public record CreateTransferChannelOutput(UUID id) {

    public static CreateTransferChannelOutput from(final TransferChannel transferChannel) {
        return new CreateTransferChannelOutput(transferChannel.getId().getValue());
    }

}
