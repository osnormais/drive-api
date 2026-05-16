package org.osnormais.drive.api.domain.transferchannel.valueobject;

import org.osnormais.drive.api.domain.ValueObject;
import org.osnormais.drive.api.domain.validation.ValidationError;
import org.osnormais.drive.api.domain.validation.handler.ValidationHandler;

public record ChunkSize(long bytes) implements ValueObject {

    public static ChunkSize of(final long bytes) {
        return new ChunkSize(bytes);
    }

    @Override
    public void validate(final ValidationHandler handler) {

        if (bytes <= 0)
            handler.append(ValidationError.with("ChunkSize.bytes must be greater than 0"));

    }

}
