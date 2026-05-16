package org.osnormais.drive.api.domain.transferchannel.valueobject;

import java.time.Instant;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.transferchannel.TransferChannelType;

public record ChunkPermission(
        Long chunkIndex,
        TransferChannelType transferChannelType,
        Instant expiresAt) implements ValueObject {

    public static ChunkPermission create(
            Long chunkIndex,
            TransferChannelType transferChannelType,
            Instant expiresAt) {
        return new ChunkPermission(chunkIndex, transferChannelType, expiresAt);
    }

}
