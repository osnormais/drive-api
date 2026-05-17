package org.osnormais.drive.api.infrastructure.transferchannel.gateway;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.osnormais.drive.api.application.gateway.transferchannel.TransferChannelCommandGateway;
import org.osnormais.drive.api.application.gateway.transferchannel.TransferChannelQueryGateway;
import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelId;
import org.springframework.stereotype.Component;

@Component
public class TransferChannelInMemGateway implements TransferChannelQueryGateway, TransferChannelCommandGateway {

    private static final Map<TransferChannelId, TransferChannel> TRANSFER_CHANNELS = new HashMap<>();

    @Override
    public TransferChannel create(final TransferChannel transferChannel) {
        TRANSFER_CHANNELS.put(transferChannel.getId(), transferChannel);
        return transferChannel;
    }

    @Override
    public Optional<TransferChannel> findById(final TransferChannelId id) {
        return Optional.ofNullable(TRANSFER_CHANNELS.get(id));
    }

}
