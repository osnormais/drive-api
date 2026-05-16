package org.osnormais.drive.api.infrastructure.transferchannel.data.rest;

import java.time.Instant;

public record GetTransferChannelTokenResponse(Instant expiresAt, String token) {

}
