package org.osnormais.drive.api.domain.transferchannel.valueobject;

import java.time.Instant;

import org.osnormais.drive.api.domain.ValueObject;

public record TransferChannel(
        TransferChannel.Type type,
        Instant expiresAt,
        ThroughputLimit throughputLimit,
        ChunkSpecification chunkSpecification) implements ValueObject {

    public enum Type {
        UPLOAD,
        DOWNLOAD
    }
}
