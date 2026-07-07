package org.osnormais.drive.api.application.gateway.transferchannel;

import java.util.Optional;

import org.osnormais.drive.api.domain.transferchannel.TransferChannel;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelId;

public interface TransferChannelQueryGateway {

    Optional<TransferChannel> findById(TransferChannelId id);

}
