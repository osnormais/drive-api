package org.osnormais.drive.api.domain.transferchannel.valueobject;

import static java.util.Objects.isNull;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record ChunkSpecification(
        ThroughputLimit throughputLimit,
        ChunkSize chunkSize,
        ParallelChunkLimit parallelChunkLimit) implements ValueObject {

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(throughputLimit))
            handler.append(ValidationError.with("ChunkSpecification.throughputLimit' should not be null"));
        else
            throughputLimit.validate(handler);

        if (isNull(chunkSize))
            handler.append(ValidationError.with("ChunkSpecification.chunkSize' should not be null"));
        else
            chunkSize.validate(handler);

        if (isNull(parallelChunkLimit))
            handler.append(ValidationError.with("ChunkSpecification.parallelChunkLimit' should not be null"));
        else
            parallelChunkLimit.validate(handler);

    }

}
