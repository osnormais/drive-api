package org.osnormais.drive.api.infrastructure.transferchannel.data.rest;

import java.time.Instant;
import java.util.UUID;

public record GetTransferChannelResponse(UUID id, Long totalChunks, Instant expiresAt) {

}
