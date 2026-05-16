package org.osnormais.drive.api.domain.transferchannel.valueobject;

import java.time.Duration;
import java.time.Instant;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.transferchannel.Type;

public record ChunkPermission(
        Long chunkIndex,
        Type transferChannelType,
        Instant expiresAt) implements ValueObject {

    public static ChunkPermission create(
            Long chunkIndex,
            Type transferChannelType,
            Duration validDuration) {
        final Instant expiresAt = Instant.now().plus(validDuration);
        return new ChunkPermission(chunkIndex, transferChannelType, expiresAt);
    }

}
