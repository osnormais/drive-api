package org.osnormais.drive.api.infrastructure.transferchannel.data.rest;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record GetTransferChannelTokensResponse(UUID fileId, Instant expiresAt, Set<ChunkToken> chunkTokens) {

    public record ChunkToken(Long index, Long offset, Long size, String token) {

    }

}
