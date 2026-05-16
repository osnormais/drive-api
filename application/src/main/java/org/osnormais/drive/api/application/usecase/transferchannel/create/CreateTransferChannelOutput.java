package org.osnormais.drive.api.application.usecase.transferchannel.create;

import java.util.UUID;

public record CreateTransferChannelOutput(UUID fileId, Long targetBytesPerSecond) {

}
