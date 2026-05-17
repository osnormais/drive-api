package org.osnormais.drive.api.infrastructure.transferchannel.data.rest;

import java.time.Instant;
import java.util.UUID;

public record GetTransferChannelTokenResponse(UUID id, Instant expiresAt, String token) {

}
