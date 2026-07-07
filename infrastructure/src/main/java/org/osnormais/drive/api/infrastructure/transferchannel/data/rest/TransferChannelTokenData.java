package org.osnormais.drive.api.infrastructure.transferchannel.data.rest;

import java.time.Instant;
import java.util.Map;

public record TransferChannelTokenData(
        String actor,
        Instant expiresAt,
        String fileId,
        String type,
        Integer maxParallelChunks,
        Long throughputLimit,
        Long chunkIndex,
        Long chunkOffset,
        Long chunkSize) {

    public Map<String, Object> toMap() {

        return Map.<String, Object>of(
                "actor", actor,
                "file", fileId,
                "type", type,
                "maxParallelChunks", maxParallelChunks,
                "throughputLimit", throughputLimit,
                "chunkIndex", chunkIndex,
                "chunkOffset", chunkOffset,
                "chunkSize", chunkSize);

    }

}
