package org.osnormais.drive.api.application.gateway.transferchannel;

import org.osnormais.drive.api.domain.transferchannel.TransferChannel;

public interface TransferChannelCommandGateway {

    void create(TransferChannel transferChannel);

}
