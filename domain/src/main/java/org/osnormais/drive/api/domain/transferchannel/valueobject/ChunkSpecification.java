package org.osnormais.drive.api.domain.transferchannel.valueobject;

import static java.util.Objects.isNull;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record ChunkSpecification(ThroughputLimit throughputLimit) implements ValueObject {

    public static ChunkSpecification create(final ThroughputLimit throughputLimit) {
        return new ChunkSpecification(throughputLimit);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (isNull(throughputLimit))
            handler.append(ValidationError.with("Chunk 'throughputLimit' should not be null"));
        else
            throughputLimit.validate(handler);

    }

}
