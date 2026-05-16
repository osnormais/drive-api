package org.osnormais.drive.api.application.usecase.transferchannel.create;

import java.util.UUID;

import org.osnormais.drive.api.domain.transferchannel.Type;

public record CreateTransferChannelInput(
        UUID userId,
        UUID fileId,
        Type transferChannelType) {

}
