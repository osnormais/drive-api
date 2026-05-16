package org.osnormais.drive.api.domain.transferchannel.valueobject;

import static java.util.Objects.isNull;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record ChunkSpecification(
        ChunkSize size,
        ThroughputLimit throughputLimit,
        ParallelChunkLimit maxParallel) implements ValueObject {

    public static ChunkSpecification create(
            ChunkSize size,
            ThroughputLimit throughputLimit,
            ParallelChunkLimit maxParallel) {
        return new ChunkSpecification(size, throughputLimit, maxParallel);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(size))
            handler.append(ValidationError.with("Chunk 'size' should not be null"));
        else
            size.validate(handler);

        if (isNull(throughputLimit))
            handler.append(ValidationError.with("Chunk 'throughputLimit' should not be null"));
        else
            throughputLimit.validate(handler);

        if (isNull(maxParallel))
            handler.append(ValidationError.with("Chunk 'maxParallel' should not be null"));
        else
            maxParallel.validate(handler);

    }

}
